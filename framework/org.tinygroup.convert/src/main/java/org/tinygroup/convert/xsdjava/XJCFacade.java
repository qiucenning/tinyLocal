/**
 *  Copyright (c) 1997-2013, tinygroup.org (luo_guo@live.cn).
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
 * --------------------------------------------------------------------------
 *  版权 (c) 1997-2013, tinygroup.org (luo_guo@live.cn).
 *
 *  本开源软件遵循 GPL 3.0 协议;
 *  如果您不遵循此协议，则不被允许使用此文件。
 *  你可以从下面的地址获取完整的协议文本
 *
 *       http://www.gnu.org/licenses/gpl.html
 */
package org.tinygroup.convert.xsdjava;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * A shabby driver to invoke XJC1 or XJC2 depending on the command line switch.
 * <p/>
 * <p/>
 * This class is compiled with -source 1.2 so that we can report a nice
 * user-friendly "you require Tiger" error message.
 *
 * @author Kohsuke Kawaguchi
 */
public class XJCFacade {

    @SuppressWarnings("rawtypes")
    public static void execute(String[] args) throws Exception {

        try {
            ClassLoader cl = SecureLoader.getClassClassLoader(XJCFacade.class);

            Class driver = cl
                    .loadClass("org.tinygroup.convert.xsdjava.DriverWrapper");
            Method mainMethod = driver.getDeclaredMethod("main",
                    new Class[]{String[].class});
            try {
                mainMethod.invoke(null, new Object[]{args});
            } catch (IllegalAccessException e) {
                throw e;
            } catch (InvocationTargetException e) {
                throw e;
            }
        } catch (UnsupportedClassVersionError e) {
            System.err
                    .println("XJC requires JDK 5.0 or later. Please download it from http://java.sun.com/j2se/1.5/");
        }
    }


}
