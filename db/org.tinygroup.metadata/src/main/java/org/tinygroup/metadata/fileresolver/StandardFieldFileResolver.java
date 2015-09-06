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
package org.tinygroup.metadata.fileresolver;

import java.io.IOException;
import java.io.InputStream;

import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.metadata.config.stdfield.StandardFields;
import org.tinygroup.metadata.stdfield.StandardFieldProcessor;
import org.tinygroup.metadata.util.MetadataUtil;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xstream.XStreamFactory;

import com.thoughtworks.xstream.XStream;

public class StandardFieldFileResolver extends AbstractFileProcessor {

	private static final String STANDARDFIELD_EXTFILENAME = ".stdfield.xml";

	public boolean isMatch(FileObject fileObject) {
		return fileObject.getFileName().endsWith(STANDARDFIELD_EXTFILENAME);
	}

	public void process() {
		StandardFieldProcessor standardFieldProcessor = SpringUtil
				.getBean(MetadataUtil.STDFIELDPROCESSOR_BEAN);
		XStream stream = XStreamFactory
				.getXStream(MetadataUtil.METADATA_XSTREAM);
		for (FileObject fileObject : deleteList) {
			logger.logMessage(LogLevel.INFO, "正在移除stdfield文件[{0}]",
					fileObject.getAbsolutePath());
			StandardFields standardFields = (StandardFields)caches.get(fileObject.getAbsolutePath());
			if(standardFields!=null){
				standardFieldProcessor.removeStandardFields(standardFields);
				caches.remove(fileObject.getAbsolutePath());
			}
			logger.logMessage(LogLevel.INFO, "移除stdfield文件[{0}]结束",
					fileObject.getAbsolutePath());
		}
		for (FileObject fileObject : changeList) {
			logger.logMessage(LogLevel.INFO, "正在加载stdfield文件[{0}]",
					fileObject.getAbsolutePath());
			StandardFields oldStandardFields = (StandardFields)caches.get(fileObject.getAbsolutePath());
			if(oldStandardFields!=null){
				standardFieldProcessor.removeStandardFields(oldStandardFields);
			}
			InputStream inputStream = fileObject.getInputStream();
			StandardFields standardFields = (StandardFields) stream
					.fromXML(inputStream);
			try {
				inputStream.close();
			} catch (IOException e) {
				logger.errorMessage("关闭流时发生异常,文件路径:{}",e,fileObject.getAbsolutePath());
			}
			standardFieldProcessor.addStandardFields(standardFields);
			caches.put(fileObject.getAbsolutePath(), standardFields);
			logger.logMessage(LogLevel.INFO, "加载stdfield文件[{0}]结束",
					fileObject.getAbsolutePath());
		}
	}

}
