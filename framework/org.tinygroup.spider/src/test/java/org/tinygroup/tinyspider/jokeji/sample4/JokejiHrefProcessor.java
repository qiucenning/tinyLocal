/**
 *  Copyright (c) 1997-2013, www.tinygroup.org (luo_guo@icloud.com).
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
 */
package org.tinygroup.tinyspider.jokeji.sample4;

import org.tinygroup.htmlparser.node.HtmlNode;
import org.tinygroup.tinyspider.Processor;
import org.tinygroup.tinyspider.UrlUtils;

import java.util.Map;

public class JokejiHrefProcessor implements Processor {
    public void process(String url, HtmlNode node, Map<String,Object> parameters) {
        String href = node.getAttribute("href");
        if (href == null || href.toLowerCase().startsWith("javascript:")) {
            return;
        }
        try {
            String realUrl = UrlUtils.getUrl(url, href);
            if (realUrl.toLowerCase().indexOf("http://www.jokeji.cn") >= 0) {
                JokejiTest.processUrl(realUrl);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}