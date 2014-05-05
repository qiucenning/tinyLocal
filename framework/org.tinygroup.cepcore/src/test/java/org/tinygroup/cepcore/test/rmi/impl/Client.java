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
package org.tinygroup.cepcore.test.rmi.impl;

import org.tinygroup.cepcore.test.rmi.RMIInterface;
import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.event.Event;
import org.tinygroup.event.ServiceRequest;

import java.rmi.Naming;

public class Client {
	public static void main(String[] args) {
		try {
			// LocateRegistry.createRegistry(8808);
			// Naming.rebind("//localhost:8808/CEP-SERVER", new RMIImpl());

			String url = "//192.168.84.57:8808/CEP-SERVER";
			RMIInterface RmiObject = (RMIInterface) Naming.lookup(url);
			long s = System.currentTimeMillis();
			for (int i = 0; i < 1000; i++) {
				RmiObject.deal(getRegEvent());
			}
			long e = System.currentTimeMillis();
			System.out.println("time:" + (e - s));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Event getRegEvent() {
		Event e = new Event();
		e.setEventId("aaa");
		ServiceRequest request = new ServiceRequest();
		request.setServiceId("cepNodeRegisteNode");
		Context c = new ContextImpl();

		c.put("node", "a");
//		for (int i = 0; i < 100; i++) {
//			c.put("node" + i, "a" + i);
//		}
		request.setContext(c);
		e.setServiceRequest(request);
		return e;
	}
}
