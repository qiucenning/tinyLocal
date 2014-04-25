package org.tinygroup.dbf;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by luoguo on 2014/4/25.
 */
public class DbfReader {
    String encode = "GBK";
    FileChannel fileChannel;
    DbfHeader dbfHeader;
    List<DbfField> dbfFields;
    private boolean isRemoved;

    DbfReader(File dbfFile, String encode) throws IOException {
        this(dbfFile);
        this.encode = encode;
    }

    private List<DbfField> readFields() throws IOException {
        List<DbfField> dbfFieldList = new ArrayList<DbfField>();
        for (int i = 0; i < (dbfHeader.headerLength - 33) / 32; i++) {
            dbfFieldList.add(readField());
        }
        return dbfFieldList;
    }

    public void moveFirst() throws IOException {
        fileChannel.position(dbfHeader.headerLength);
    }

    /**
     * @param position 从1开始
     * @throws IOException
     */
    public void absolute(int position) throws IOException {
        fileChannel.position(dbfHeader.headerLength + (position - 1) * dbfHeader.recordLength);
    }

    private DbfField readField() throws IOException {
        DbfField field = new DbfField();
        field.reader = this;
        ByteBuffer byteBuffer = ByteBuffer.allocate(32);
        readByteBuffer(byteBuffer);
        byte[] bytes = byteBuffer.array();
        field.setName(new String(bytes, 0, 11, encode).trim().split("\0")[0]);
        field.setType((char) bytes[11]);
        field.setDisplacement(getUnsignedInt(bytes, 12, 4));
        field.setLength(getUnsignedInt(bytes, 16, 1));
        field.setDecimal(getUnsignedInt(bytes, 17, 1));
        field.flag = bytes[18];
        return field;
    }

    private DbfHeader readDbfHeader() throws IOException {
        DbfHeader header = new DbfHeader();
        ByteBuffer byteBuffer = ByteBuffer.allocate(32);
        readByteBuffer(byteBuffer);
        byte[] bytes = byteBuffer.array();
        header.version = bytes[0];
        header.lastUpdate = (getUnsignedInt(bytes, 1, 1) + 1900) * 10000 + getUnsignedInt(bytes, 2, 1) * 100 + getUnsignedInt(bytes, 3, 1);
        header.recordCount = getUnsignedInt(bytes, 4, 4);
        header.headerLength = getUnsignedInt(bytes, 8, 2);
        header.recordLength = getUnsignedInt(bytes, 10, 2);
        System.out.println(header.getFileType());
        return header;
    }

    int getUnsignedInt(byte[] bytes, int start, int length) {
        int value = 0;
        for (int i = 0; i < length; i++) {
            value += getIntValue(bytes[start + i], i);
        }
        return value;
    }

    int getIntValue(byte b, int bytePos) {
        int v = 1;
        for (int i = 0; i < bytePos; i++) {
            v = v * 256;
        }
        return getUnsignedInt(b) * v;
    }

    int getUnsignedInt(byte byteValue) {
        if (byteValue < 0) {
            return byteValue + 256;
        } else {
            return byteValue;
        }
    }

    DbfReader(File dbfFile) throws IOException {
        RandomAccessFile aFile = new RandomAccessFile(dbfFile, "r");

        fileChannel = aFile.getChannel();
        dbfHeader = readDbfHeader();
        dbfFields = readFields();
        skipHeaderTerminator();
    }

    private void skipHeaderTerminator() throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1);
        readByteBuffer(byteBuffer);
    }

    DbfReader(String file, String encode) throws IOException {
        this(new File(file), encode);
    }

    DbfReader(String file) throws IOException {
        this(new File(file));
    }

    public void close() throws IOException {
        fileChannel.close();
    }

    public void readRecord() throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1);
        readByteBuffer(byteBuffer);
        this.isRemoved = (byteBuffer.array()[0] == '*');
        for (DbfField field : dbfFields) {
            field.read();
        }
    }

    void readByteBuffer(ByteBuffer byteBuffer) throws IOException {
        int length = fileChannel.read(byteBuffer);
//        fileChannel.position(fileChannel.position() + length);
    }
}
