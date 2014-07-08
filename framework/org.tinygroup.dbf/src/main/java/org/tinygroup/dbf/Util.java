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
package org.tinygroup.dbf;

/**
 * Created by luoguo on 2014/4/26.
 */
public final class Util {

    private Util() {}

    public static final int MAX_MINI_UINT = 256;

    public static int getUnsignedInt(byte[] bytes, int start, int length) {
        int value = 0;
        for (int i = 0; i < length; i++) {
            value += getIntValue(bytes[start + i], i);
        }
        return value;
    }

    public static int getIntValue(byte b, int bytePos) {
        int v = 1;
        for (int i = 0; i < bytePos; i++) {
            v = v << 8;
        }
        return getUnsignedInt(b) * v;
        
    }

    public static int getUnsignedInt(byte byteValue) {
        if (byteValue < 0) {
            return byteValue + MAX_MINI_UINT;
        } else {
            return byteValue;
        }
    }
    ///added by wcg;
    public static byte[] getByteFromInt(int value,int bit) {
    	byte[] result = new byte[bit];
        byte[] abyte0 = new byte[4];
        abyte0[0] = (byte) (0xff & value);
        abyte0[1] = (byte) ((0xff00 & value) >> 8);
        abyte0[2] = (byte) ((0xff0000 & value) >> 16);
        abyte0[3] = (byte) ((0xff000000 & value) >> 24);
        System.arraycopy(abyte0, 0, result, 0, bit);
    	return result;
    }
    

    public static Boolean getBooleanValue(String stringValue) {
        String value = stringValue.toLowerCase();
        if (value.equals("t") || value.equals("y")) {
            return true;
        }
        if (value.equals("f") || value.equals("n")) {
            return false;
        }
        return null;
    }
}
