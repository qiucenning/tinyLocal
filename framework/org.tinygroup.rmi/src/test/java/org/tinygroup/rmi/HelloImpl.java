/**
 *  Copyright (c) 1997-2013, www.tinygroup.org (tinygroup@126.com).
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
package org.tinygroup.rmi;

import java.io.Serializable;
import java.rmi.RemoteException;

/**
 * Created by luoguo on 14-1-24.
 */
public class HelloImpl implements Hello, Serializable {
    public HelloImpl() throws RemoteException {
        System.out.println("创建：" + HelloImpl.class + "实例");
    }

    public String sayHello(String name) throws RemoteException {
        return "Hello," + name;
    }
}
