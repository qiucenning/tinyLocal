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
package org.tinygroup.htmlparser;

import java.io.File;

import org.tinygroup.commons.file.IOUtils;
import org.tinygroup.htmlparser.parser.HtmlStringParser;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.VFS;

public class MaoHaoTest {
	public static void main(String[] args) throws Exception {
		File file1 = new File("src/test/resources/dtd.html");
		System.out.println(file1.getAbsolutePath());
		HtmlStringParser parser = new HtmlStringParser();
		FileObject file = VFS.resolveFile("file:" + file1.getAbsolutePath());
		HtmlDocument doc = parser.parse(IOUtils.readFromInputStream(file.getInputStream(), "UTF-8"));
		System.out.println(doc.toString());
	}
}
