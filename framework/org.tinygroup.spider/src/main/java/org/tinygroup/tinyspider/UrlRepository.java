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

import java.util.Map;

/**
 * URL仓库
 * 
 * @author luoguo
 * 
 */
public interface UrlRepository {
	/**
	 * 返回url是否已经在仓库中存在
	 * 
	 * @param url
	 * @return
	 */
	boolean isExist(String url);

	/**
	 * 返回url是否已经在仓库中存在，带有参数
	 * 
	 * @param url
	 * @param parameter
	 * @return
	 */
	boolean isExist(String url, Map<String, Object> parameter);

	/**
	 * 如果不存在，则放放，如果已经存在，则替换
	 * 
	 * @param url
	 * @param content
	 */
	void putUrlWithContent(String url, String content);

	/**
	 * 如果不存在，则放放，如果已经存在，则替换
	 * 
	 * @param url
	 * @param parameter
	 * @param content
	 */
	void putUrlWithContent(String url, Map<String, Object> parameter,
			String content);

	/**
	 * 如果存在，则返回内容；如果不存在，则抛出运行时异常
	 * 
	 * @param url
	 * @return
	 */
	String getContent(String url);

	/**
	 * 如果存在，则返回内容；如果不存在，则抛出运行时异常
	 * 
	 * @param url
	 * @param parameter
	 * @return
	 */
	String getContent(String url, Map<String, Object> parameter);
}
