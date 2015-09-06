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
package org.tinygroup.tinydb.fileresolver;

import java.io.IOException;
import java.io.InputStream;

import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.tinydb.BeanOperatorManager;
import org.tinygroup.tinydb.relation.Relations;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xstream.XStreamFactory;

import com.thoughtworks.xstream.XStream;

/**
 * 
 * 功能说明: 授权配置文件加载器

 * 开发人员: renhui <br>
 * 开发时间: 2013-7-15 <br>
 * <br>
 */
public class RelationFileProcessor  extends AbstractFileProcessor{
	
	private static final String RELATION_CONFIG_FILE_EXT_NAME=".relation.xml";

	public boolean isMatch(FileObject fileObject) {
		return fileObject.getFileName().endsWith(RELATION_CONFIG_FILE_EXT_NAME);
	}

	public void process() {
		BeanOperatorManager manager=SpringUtil.getBean(BeanOperatorManager.OPERATOR_MANAGER_BEAN);
		XStream stream = XStreamFactory
				.getXStream(BeanOperatorManager.XSTEAM_PACKAGE_NAME);
		for (FileObject fileObject : deleteList) {
			logger.logMessage(LogLevel.INFO, "正在移除数据库关联配置文件[{0}]",
					fileObject.getAbsolutePath());
			Relations relations=(Relations)caches.get(fileObject.getAbsolutePath());
			if(relations!=null){
				manager.removeRelationConfigs(relations);
				caches.remove(fileObject.getAbsolutePath());
			}
			logger.logMessage(LogLevel.INFO, "移除数据库关联配置文件[{0}]结束",
					fileObject.getAbsolutePath());
		}
		for (FileObject fileObject : changeList) {
			logger.logMessage(LogLevel.INFO, "正在加载数据库关联配置文件[{0}]",
					fileObject.getAbsolutePath());
			Relations oldRelations=(Relations)caches.get(fileObject.getAbsolutePath());
			if(oldRelations!=null){
				manager.removeRelationConfigs(oldRelations);
			}	
			InputStream inputStream = fileObject.getInputStream();
			Relations relations=(Relations) stream.fromXML(inputStream);
			try {
				inputStream.close();
			} catch (IOException e) {
				logger.errorMessage("关闭流时发生异常,文件路径:{}",e,fileObject.getAbsolutePath());
			}
			manager.addRelationConfigs(relations);
			caches.put(fileObject.getAbsolutePath(), relations);
			logger.logMessage(LogLevel.INFO, "加载数据库关联配置文件[{0}]结束",
					fileObject.getAbsolutePath());
		}
		
	}

	public void process(ClassLoader loader) {
		process();
		
	}

}
