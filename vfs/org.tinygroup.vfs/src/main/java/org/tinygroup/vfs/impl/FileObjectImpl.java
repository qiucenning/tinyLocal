/**
 *  Copyright (c) 1997-2013, www.tinygroup.org (luo_guo@icloud.com).
 *
 *  Licensed under the GPL, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.gnu.org/licenses/gpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.tinygroup.vfs.impl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.SchemaProvider;
import org.tinygroup.vfs.VFS;
import org.tinygroup.vfs.VFSRuntimeException;

public class FileObjectImpl extends AbstractFileObject {

    private String path;
    private List<FileObject> children;
    private File file = null;
    long lastModifiedTime = 0;

    public FileObjectImpl(SchemaProvider schemaProvider, String resource) {
        super(schemaProvider);

        init(new File(resource));
    }

    public boolean isModified() {
        return lastModifiedTime != file.lastModified();
    }

    public void resetModified() {
        lastModifiedTime = file.lastModified();
    }


    private void init(File file) {
        this.file = file;
    }

    public String toString() {
        return file.getAbsolutePath();
    }

    public FileObjectImpl(SchemaProvider schemaProvider, File file) {
        super(schemaProvider);
        init(file);
    }

    public String getFileName() {
        return file.getName();
    }

    public String getPath() {
        // 如果没有计算过
        if (path == null) {
            // 如果有父亲
            if (getParent() != null) {
                path = getParent().getPath() + "/" + getFileName();
            } else {
                if (file.isDirectory()) {
                    return "";
                } else {
                    return "/" + file.getName();
                }
            }
        }
        return path;
    }

    public String getAbsolutePath() {
        return file.getAbsolutePath();
    }

    public String getExtName() {
        int lastIndexOfDot = file.getName().lastIndexOf(".");
        if (lastIndexOfDot == -1) {
            // 如果不存在
            return null;
        }
        return file.getName().substring(lastIndexOfDot + 1);
    }

    public long getSize() {
        if (file.exists() && file.isFile()) {
            return file.length();
        }
        return 0;
    }

    public InputStream getInputStream() {
        try {
            if (file.exists() && file.isFile()) {
                return new BufferedInputStream(new FileInputStream(file));
            } else {
                return null;
            }
        } catch (FileNotFoundException e) {
            throw new VFSRuntimeException(file.getAbsolutePath()
                    + "获取FileInputStream出错，原因" + e);
        }
    }

    public boolean isFolder() {
        if (file.exists()) {
            return file.isDirectory();
        }
        return false;
    }

    public List<FileObject> getChildren() {
        if (isModified()) {
            forEachFile();
        }
        return children;
    }

    private void forEachFile() {
        if (children == null) {
            children = new ArrayList<FileObject>();
        }
        if (file.exists() && file.isDirectory()) {
            File[] subFiles = file.listFiles();
            removeDeletedFileObject();
            for (File subFile : subFiles) {
                 if (subFile.getName().endsWith(".jar")) {
                     children.add(VFS.resolveFile(subFile.getAbsolutePath()));
                } else {
                     if(!contains(file)){//如果不包含文件，则添加之
                         FileObjectImpl fileObject = new FileObjectImpl(getSchemaProvider(),
                                 subFile);
                         fileObject.setParent(this);
                         children.add(fileObject);
                     }
                }
            }
        }
    }

    private void removeDeletedFileObject() {
        for(int i=children.size()-1;i>=0;i--){
            FileObject fileObject=children.get(i);
            if(fileObject instanceof FileObjectImpl){
                if(!fileObject.isExist()){
                    children.remove(fileObject);
                }
            }else {
                children.remove(fileObject);
            }
        }
    }

    boolean contains(File file){
        for(FileObject fileObject:children){
            if(FileObjectImpl.class.isInstance(fileObject)){
                FileObjectImpl fileObjectImpl= (FileObjectImpl) fileObject;
                if(fileObjectImpl.file.equals(file)){
                    return true;
                }
            }
        }
        return false;
    }

    public long getLastModifiedTime() {
        return file.lastModified();
    }

    public boolean isExist() {
        return file.exists();
    }

    public boolean isInPackage() {
        return false;
    }

    public FileObject getChild(String fileName) {
        if (getChildren() != null) {
            for (FileObject fileObject : getChildren()) {
                if (fileObject.getFileName().equals(fileName)) {
                    return fileObject;
                }
            }
        }
        return null;
    }

    public URL getURL() {
        try {
            return new URL(FileSchemaProvider.FILE_PROTOCOL + getAbsolutePath());
        } catch (MalformedURLException e) {
            throw new VFSRuntimeException(e);
        }
    }

    public OutputStream getOutputStream() {
        try {
            return new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            throw new VFSRuntimeException(e);
        }
    }

}
