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
package org.tinygroup.plugin.fileprocessor;

import java.io.IOException;
import java.io.InputStream;

import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.plugin.PluginManager;
import org.tinygroup.plugin.config.PluginConfigs;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xstream.XStreamFactory;
import com.thoughtworks.xstream.XStream;

public class PluginFileProcessor extends AbstractFileProcessor {
	
	private static final String PLUGIN_EXT_FILENAME = ".plugin.xml";
	private static final String PLUGIN_XSTREAM = "plugin";
	private PluginManager manager;
	
	public PluginManager getManager() {
		return manager;
	}

	public void setManager(PluginManager manager) {
		this.manager = manager;
	}

	public boolean isMatch(FileObject fileObject) {
		return fileObject.getFileName().endsWith(PLUGIN_EXT_FILENAME);
	}

	public void process() {
		XStream stream = XStreamFactory.getXStream(PLUGIN_XSTREAM);
		for (FileObject file : deleteList) {
			logger.logMessage(LogLevel.INFO, "移除plugin配置文件:{0}",
					file.getFileName());
			PluginConfigs configs = (PluginConfigs) caches.get(file.getAbsolutePath());
			if(configs!=null){
				manager.removePlugin(configs);
				caches.remove(file.getAbsolutePath());
			}
			logger.logMessage(LogLevel.INFO, "移除plugin配置文件:{0}完成",
					file.getFileName());
		}
		for (FileObject file : changeList) {
			logger.logMessage(LogLevel.INFO, "开始读取plugin配置文件:{0}",
					file.getFileName());
			PluginConfigs oldConfigs = (PluginConfigs) caches.get(file.getAbsolutePath());
			if(oldConfigs!=null){
				manager.removePlugin(oldConfigs);
			}
			InputStream inputStream = file
					.getInputStream();
			PluginConfigs configs = (PluginConfigs) stream.fromXML(inputStream);
			try {
				inputStream.close();
			} catch (IOException e) {
				logger.errorMessage("关闭流时发生异常,文件路径:{}",e,file.getAbsolutePath());
			}
			manager.addPlugin(configs);
			caches.put(file.getAbsolutePath(), configs);
			logger.logMessage(LogLevel.INFO, "读取plugin配置文件:{0}完成",
					file.getFileName());
		}
	}

}
