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
package org.tinygroup.rmi.test;

import java.io.Serializable;
import java.rmi.RemoteException;

/**
 * Created by luoguo on 14-1-24.
 */
public class HelloImpl implements Hello, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2865990858526782951L;
	private transient static int i = 0;

	public HelloImpl() throws RemoteException {
		System.out.println("创建：" + HelloImpl.class + "实例");
	}

	public String sayHello(String name) throws RemoteException {
		i++;
		if (i % 5000 == 0){
			System.out.println(System.currentTimeMillis()+"------" + i + "------------");
		}
		System.out.println("Hello," + name);	
		return "Hello," + name;
	}

	public void verify() throws RemoteException {
		// TODO Auto-generated method stub

	}
}
