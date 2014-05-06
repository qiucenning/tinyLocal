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
package org.tinygroup.rpc.util;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.event.Event;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.rpc.CEPCoreRMI;
import org.tinygroup.springutil.SpringUtil;

public class CEPCoreRMIServer extends UnicastRemoteObject implements CEPCoreRMI {

	private static final long serialVersionUID = -8595995795665087127L;
	private static Logger logger = LoggerFactory
			.getLogger(CEPCoreRMIServer.class);

	public CEPCoreRMIServer() throws RemoteException {
		super();
	}

	public Event processFromRemote(Event event) throws RemoteException {
		logger.logMessage(LogLevel.DEBUG,
				"接收到远程请求,serivceId:{0}", event
						.getServiceRequest().getServiceId());
		CEPCore cep = SpringUtil.getBean(CEPCore.CEP_CORE_BEAN);
		cep.process(event);
		
		logger.logMessage(LogLevel.DEBUG,
				"处理远程请求完毕,serivceId:{0}", event
						.getServiceRequest().getServiceId());
		return event;
	}

}
