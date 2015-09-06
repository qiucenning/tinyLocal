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
package org.tinygroup.service.config.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.service.Service;
import org.tinygroup.service.config.ServiceComponent;
import org.tinygroup.service.config.ServiceComponents;
import org.tinygroup.service.config.XmlConfigServiceLoader;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.VFS;
import org.tinygroup.xstream.XStreamFactory;

import com.thoughtworks.xstream.XStream;

public class DefaultXmlConfigServiceLoader extends XmlConfigServiceLoader {
	private static final String SERVICE_FILE_EXTENSION = ".service.xml";
	private static Logger logger = LoggerFactory
			.getLogger(DefaultXmlConfigServiceLoader.class);
	private String path;
	private List<ServiceComponents> list = new ArrayList<ServiceComponents>();

	
	protected List<ServiceComponents> getServiceComponents() {
		load();
		return list;
	}

	public void setConfigPath(String path) {
		this.path = path;
	}

	private void load() {
		logger.logMessage(LogLevel.DEBUG, "开始扫描Serivce文件");
		FileObject file = VFS.resolveFile(path);
		load(file);

		logger.logMessage(LogLevel.DEBUG, "Serivce文件扫描结束");
	}

	private void load(FileObject file) {
		if (file.isFolder()) {
			loadDir(file);
		} else {
			loadFile(file);
		}
	}

	private void loadFile(FileObject file) {
		logger.logMessage(LogLevel.DEBUG, "开始扫描文件{0}", file.getAbsolutePath());
		if (file.getFileName().endsWith(SERVICE_FILE_EXTENSION)) {
			XStream xStream = XStreamFactory
					.getXStream(Service.SERVICE_XSTREAM_PACKAGENAME);
			ServiceComponents components = null;
			InputStream inputStream = file
					.getInputStream();
			components = (ServiceComponents) xStream.fromXML(inputStream);
			try {
				inputStream.close();
			} catch (IOException e) {
				logger.errorMessage("关闭流时发生异常,文件路径:{}",e,file.getAbsolutePath());
			}
			list.add(components);
			logger.logMessage(LogLevel.DEBUG, "添加ServiceComponents");
		}

		logger.logMessage(LogLevel.DEBUG, "扫描文件{0}结束", file.getAbsolutePath());
	}

	private void loadDir(FileObject file) {
		logger.logMessage(LogLevel.DEBUG, "开始扫描目录{0}", file.getAbsolutePath());
		for (FileObject o : file.getChildren()) {
			load(o);
		}
		logger.logMessage(LogLevel.DEBUG, "扫描目录{0}结束", file.getAbsolutePath());
	}

	protected Object getServiceInstance(ServiceComponent component)
			throws Exception {
		if (component.getBean() == null
				|| "".equals(component.getBean().trim())) {
			Class<?> clazz = Class.forName(component.getType());
			return SpringUtil.getBean(clazz);
		}
		return SpringUtil.getBean(component.getBean());
	}

}
