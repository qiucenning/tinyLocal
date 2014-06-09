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

import java.rmi.Remote;
import java.rmi.RemoteException;

import org.tinygroup.rmi.RmiServer;
import org.tinygroup.rmi.impl.RmiServerLocal;
import org.tinygroup.rmi.impl.RmiServerRemote;

public class RmiRunClient {
	private static String SERVERIP = "192.168.84.23";
	private static String LOCALIP = "192.168.84.23";

	public static void main(String[] args) {
		RmiServer remoteServer = null;
		RmiServerLocal localServer = null;
		try {
		
			remoteServer = new RmiServerRemote(SERVERIP, 8888);
			localServer = new RmiServerLocal(LOCALIP, 7777);
		
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			localServer.registerLocalObject(new HelloImpl(), "hello1");
			remoteServer.registerRemoteObject((Remote)localServer.getRemoteObject("hello1"), "hello1");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RmiRunClient c = new RmiRunClient(remoteServer);
		c.run();
		
	}

	private RmiServer remoteServer;

	public RmiRunClient(RmiServer remoteServer) {
		this.remoteServer = remoteServer;
	}

	public void run() {
		MyThread t = new MyThread();
		t.run();
	}

	class MyThread extends Thread {
		boolean end = false;

		public void run() {
			while (!end) {
				hello();
				try {
					sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

		public void hello() {
			Hello hello = null;
			try {
				hello = remoteServer.getRemoteObject("hello");
			} catch (Exception e) {
				e.printStackTrace();
				// throw new RuntimeException("获取对象失败"+e.getMessage());
			}

			try {
				String info = hello.sayHello("abc");
//				System.out.println(info);
				if (!"Hello,abc".equals(info)) {
					throw new RuntimeException("执行结果的字符串不匹配");
				}
			} catch (Exception e) {
				e.printStackTrace();
				// throw new RuntimeException("执行方法失败:"+e.getMessage());
			}
		}
	}
}
