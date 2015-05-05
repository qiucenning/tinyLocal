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
package org.tinygroup.vfs;

import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;

/**
 * jar fileobject 测试用例
 * 
 * @author renhui
 * 
 */
public class TestJarFileObject extends TestCase {

	public void testJarFileObject() throws Exception {

		String path=getClass().getResource("/vfs-0.0.1-SNAPSHOT.jar").getFile();
		FileObject fileObject = VFS.resolveFile(path);
		FileObject fo = findFileObject(fileObject, "VFS.class");
		if (fo != null) {
			InputStream inputStream = fo.getInputStream();
			byte[] buf = new byte[(int) fo.getSize()];
			inputStream.close();
			assertTrue(buf!=null);
		}

	}

	public static FileObject findFileObject(FileObject fileObject, String name) {

		if (fileObject.getFileName().equals(name)) {
			return fileObject;
		} else {
			if (fileObject.isFolder() && fileObject.getChildren() != null) {
				for (FileObject fo : fileObject.getChildren()) {
					FileObject f = findFileObject(fo, name);
					if (f != null) {
						return f;
					}
				}
			}
		}
		return null;
	}
	
	public void testWasJar() throws IOException{
		String path="wsjar:file:"+getClass().getResource("/vfs-0.0.1-SNAPSHOT.jar").getFile();
		FileObject fileObject = VFS.resolveFile(path);
		FileObject fo = findFileObject(fileObject, "VFS.class");
		if (fo != null) {
			InputStream inputStream = fo.getInputStream();
			byte[] buf = new byte[(int) fo.getSize()];
			inputStream.close();
			assertTrue(buf!=null);
		}
	}
}
