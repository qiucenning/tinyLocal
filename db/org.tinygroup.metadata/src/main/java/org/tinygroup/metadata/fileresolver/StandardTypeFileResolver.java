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
import org.tinygroup.metadata.config.stddatatype.StandardTypes;
import org.tinygroup.metadata.stddatatype.StandardTypeProcessor;
import org.tinygroup.metadata.util.MetadataUtil;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xstream.XStreamFactory;

import com.thoughtworks.xstream.XStream;

public class StandardTypeFileResolver extends AbstractFileProcessor {

	private static final String DATATYPE_EXTFILENAME = ".datatype.xml";

	public boolean isMatch(FileObject fileObject) {
		return fileObject.getFileName().endsWith(DATATYPE_EXTFILENAME);
	}

	public void process() {
		StandardTypeProcessor standardDataTypeProcessor = SpringUtil
				.getBean(MetadataUtil.STANDARDTYPEPROCESSOR_BEAN);
		XStream stream = XStreamFactory
				.getXStream(MetadataUtil.METADATA_XSTREAM);
		for (FileObject fileObject : deleteList) {
			logger.logMessage(LogLevel.INFO, "正在移除datatype文件[{0}]",
					fileObject.getAbsolutePath());
			StandardTypes standardTypes = (StandardTypes)caches.get(fileObject.getAbsolutePath());
			if (standardTypes!=null) {
				standardDataTypeProcessor.removeStandardTypes(standardTypes);
			    caches.remove(fileObject.getAbsolutePath());
			}
			logger.logMessage(LogLevel.INFO, "移除datatype文件[{0}]结束",
					fileObject.getAbsolutePath());
		}
		for (FileObject fileObject : changeList) {
			logger.logMessage(LogLevel.INFO, "正在加载datatype文件[{0}]",
					fileObject.getAbsolutePath());
			StandardTypes oldStandardTypes = (StandardTypes)caches.get(fileObject.getAbsolutePath());
			if (oldStandardTypes!=null) {
				standardDataTypeProcessor.removeStandardTypes(oldStandardTypes);
			}
			InputStream inputStream = fileObject.getInputStream();
			StandardTypes standardTypes = (StandardTypes) stream
					.fromXML(inputStream);
			try {
				inputStream.close();
			} catch (IOException e) {
				logger.errorMessage("关闭流时发生异常,文件路径:{}",e,fileObject.getAbsolutePath());
			}
			standardDataTypeProcessor.addStandardTypes(standardTypes);
			caches.put(fileObject.getAbsolutePath(), standardTypes);
			logger.logMessage(LogLevel.INFO, "加载datatype文件[{0}]结束",
					fileObject.getAbsolutePath());
		}
	}

}
