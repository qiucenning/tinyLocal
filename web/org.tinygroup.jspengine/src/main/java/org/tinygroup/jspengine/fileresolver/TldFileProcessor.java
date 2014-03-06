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
package org.tinygroup.jspengine.fileresolver;

import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.vfs.FileObject;

public class TldFileProcessor extends AbstractFileProcessor {
	
	private static final String TLD_FILE_EXT_NAME=".tld";

	public boolean isMatch(FileObject fileObject) {
		return fileObject.getFileName().endsWith(TLD_FILE_EXT_NAME);
	}

	public void process() {
		//设置符合的tld文件列表对象
		TldFileManager manager=TldFileManager.getInstance();
		for (FileObject fileObject : deleteList) {
			logger.log(LogLevel.INFO, "正在移除tld文件：<{}>",
					fileObject.getAbsolutePath());
			manager.removeTldFile(fileObject);
			logger.log(LogLevel.INFO, "移除tld文件：<{}>结束",
					fileObject.getAbsolutePath());
		}
		for (FileObject fileObject : changeList) {
			logger.log(LogLevel.INFO, "正在加载tld文件：<{}>",
					fileObject.getAbsolutePath());
			manager.addTldFile(fileObject);
			logger.log(LogLevel.INFO, "加载tld文件：<{}>结束",
					fileObject.getAbsolutePath());
		}
	}

}
