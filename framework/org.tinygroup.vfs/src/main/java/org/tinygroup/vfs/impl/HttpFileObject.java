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

import org.tinygroup.vfs.SchemaProvider;
import org.tinygroup.vfs.VFSRuntimeException;

import java.io.IOException;
import java.net.HttpURLConnection;

public class HttpFileObject extends URLFileObject {

	public HttpFileObject(SchemaProvider schemaProvider, String resource) {
		super(schemaProvider, resource);
	}

	public long getLastModifiedTime() {
		try {
			HttpURLConnection con=(HttpURLConnection) getURL().openConnection();
			return con.getLastModified();
		} catch (IOException e) {
			throw new VFSRuntimeException(e);
		}
		
	}

	public long getSize() {
		try {
			HttpURLConnection con=(HttpURLConnection) getURL().openConnection();
			return con.getContentLength();
		} catch (IOException e) {
			throw new VFSRuntimeException(e);
		}
	}

	/**
	 * http资源，都是文件，不会有路径存在
	 */
	public boolean isFolder() {
		return false;
	}
	
	

}
