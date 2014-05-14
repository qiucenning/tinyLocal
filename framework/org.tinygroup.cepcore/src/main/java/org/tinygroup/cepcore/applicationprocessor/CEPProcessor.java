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

package org.tinygroup.cepcore.applicationprocessor;
import org.tinygroup.application.Application;
import org.tinygroup.application.ApplicationProcessor;
import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.xmlparser.node.XmlNode;

public class CEPProcessor implements ApplicationProcessor{
	private static Logger logger = LoggerFactory.getLogger(CEPProcessor.class);
	private static final String CEPCOREPLUGIN_NODE_PATH = "/application/cep-node-config"; 
	private CEPCore cep ;
	private XmlNode xmlNode;
	
	public CEPCore getCep() {
		return cep;
	}

	public void setCep(CEPCore cep) {
		this.cep = cep;
	}

	public String getApplicationNodePath() {
		return CEPCOREPLUGIN_NODE_PATH;
	}

	public String getComponentConfigPath() {
		return null;
	}

	public void config(XmlNode applicationConfig, XmlNode componentConfig) {
		xmlNode = applicationConfig;
		cep.setConfig(xmlNode);
	}

	public XmlNode getComponentConfig() {
		return null;
	}

	public XmlNode getApplicationConfig() {
		return xmlNode;
	}

	public void start() {
		try {
			cep.start();
		} catch (Exception e) {
			logger.errorMessage("CEP 启动出错", e);
		}
	}

    public void init() {

    }

    public void stop() {
		try {
			cep.stop();
		} catch (Exception e) {
			logger.errorMessage("CEP 停止出错", e);
		}
	}

	public void setApplication(Application application) {
	}

	public int getOrder() {
		return DEFAULT_PRECEDENCE;
	}
}
