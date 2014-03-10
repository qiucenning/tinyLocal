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
package org.tinygroup.tinyspider;

import org.tinygroup.htmlparser.node.HtmlNode;
import org.tinygroup.parser.filter.QuickNameFilter;
import org.tinygroup.tinyspider.impl.SpiderImpl;
import org.tinygroup.tinyspider.impl.WatcherImpl;

public class oschinaTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Spider spider = new SpiderImpl();
		Watcher watcher = new WatcherImpl();
		watcher.addProcessor(new PrintOsChinaProcessor());
		QuickNameFilter<HtmlNode> nodeFilter = new QuickNameFilter<HtmlNode>();
		nodeFilter.setNodeName("div");
		nodeFilter.setIncludeAttribute("class", "qbody");
		watcher.setNodeFilter(nodeFilter);
		spider.addWatcher(watcher);
		spider.processUrl("http://www.oschina.net/question?catalog=1");
	}

}
