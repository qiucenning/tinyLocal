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
package org.tinygroup.vfs.impl;

import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.SchemaProvider;

/**
 * 
 * 功能说明: zip协议

 * 开发人员: renhui <br>
 * 开发时间: 2013-8-29 <br>
 * <br>
 */
public class ZipSchemaProvider implements SchemaProvider {
	
	public static final String ZIP = ".zip";
	public static final String ZIP_PROTOCAL = "zip:"; 
//	private static final Logger logger = LoggerFactory.getLogger(ZipSchemaProvider.class);
	public boolean isMatch(String resource) {
		String lowerCase = resource.toLowerCase();
		return lowerCase.startsWith(ZIP_PROTOCAL) || lowerCase.endsWith(ZIP);
	}

	public String getSchema() {
		return ZIP_PROTOCAL;
	}

	public FileObject resolver(String resource) {
		if (resource.startsWith(ZIP_PROTOCAL)) {
			resource = resource.substring(ZIP_PROTOCAL.length());
		} else if (resource.startsWith(FileSchemaProvider.FILE_PROTOCAL)) {
			resource = resource.substring(FileSchemaProvider.FILE_PROTOCAL.length());
		}
		if (resource.startsWith(FileSchemaProvider.FILE_PROTOCAL)) {
	        resource = resource.substring(FileSchemaProvider.FILE_PROTOCAL.length());
	     }
//		logger.logMessage(LogLevel.INFO, "jar文件资源路径:{}",resource);
		return new ZipFileObject(this, resource);
	}

}
