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

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

class Demo {
	public static void doEntry(String file) throws Exception {
		JarFile jarFile = new JarFile(file);
		Enumeration<JarEntry> entries = jarFile.entries();
		while (entries.hasMoreElements()) {
			JarEntry jarEntry = entries.nextElement();
			System.out.println("Name of Entry in Given Zip file : "
					+ jarEntry.getName());
			System.out.println("Size of Entry in Given Zip file : "
					+ jarEntry.getCompressedSize() + " byte");
			System.out.println("Actual Size of Entry : " + jarEntry.getSize()
					+ " byte");
			if (jarEntry.getName().endsWith(".js")) {
				showFile(jarFile, jarEntry);
			}
		}
	}

	private static void showFile(JarFile jarFile, JarEntry entry)
			throws Exception {

		System.out.println("---" + entry.getName() + "--start");
		BufferedInputStream inputStream = new BufferedInputStream(
				jarFile.getInputStream(entry));
		byte[]buf=new byte[inputStream.available()];
		inputStream.read(buf);
		inputStream.close();
		System.out.println(new String(buf, "UTF-8"));
		System.out.println("---" + entry.getName() + "--end");
	}

	public static void main(String[] args) throws Exception {
		Demo.doEntry("e:/dhtmlx-0.0.1-SNAPSHOT.jar");
	}
}