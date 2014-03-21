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
package org.tinygroup.httpvisit;

import org.tinygroup.httpvisit.impl.HttpVisitorImpl;

import java.util.HashMap;
import java.util.Map;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		HttpVisitor visitor = new HttpVisitorImpl();
		visitor.init();
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("type", "3");
		para.put("botid", 728);
		para.put("msg", "1111");

		Map<String, String> header = new HashMap<String, String>();
		header.put("Origin", "http://lover.zhenyao.net");
		header.put("Referer", "http://lover.zhenyao.net/lover.swf");
		visitor.setHeaderMap(header);

		System.out.println(visitor.getUrl(
				"http://lover.zhenyao.net/", null));
		System.out.println(visitor.postUrl(
				"http://lover.zhenyao.net/chat-g.json", para));
	}

}
