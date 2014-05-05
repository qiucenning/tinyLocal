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
package org.tinygroup.weblayer.webcontext.session.store.impl;

import org.tinygroup.cache.Cache;
import org.tinygroup.commons.tools.Assert;
import org.tinygroup.support.BeanSupport;
import org.tinygroup.weblayer.webcontext.session.SessionConfig;
import org.tinygroup.weblayer.webcontext.session.SessionStore;

import java.util.Map;
import java.util.Set;

import static java.util.Collections.emptyList;

/**
 * 
 * 功能说明: session 保存在缓存中的实现方案
 * <p>

 * 开发人员: renhui <br>
 * 开发时间: 2013-5-28 <br>
 * <br>
 */
public class CacheStore extends BeanSupport implements SessionStore {

	private Cache cache;

	public Cache getCache() {
		return cache;
	}

	public void setCache(Cache cache) {
		this.cache = cache;
	}

	protected void init() {
		Assert.assertNotNull(cache, "cache must not null");
	}

	public void init(String storeName, SessionConfig sessionConfig)
			throws Exception {

	}

	public Iterable<String> getAttributeNames(String sessionID,
			StoreContext storeContext) {
		Set<String> sessionData = cache.getGroupKeys(sessionID);
		if (sessionData == null) {
			return emptyList();
		} else {
			return sessionData;
		}
	}

	public Object loadAttribute(String attrName, String sessionID,
			StoreContext storeContext) {
		return cache.get(sessionID, attrName);
	}

	public void invaldiate(String sessionID, StoreContext storeContext) {
        cache.cleanGroup(sessionID);
	}

	public void commit(Map<String, Object> modifiedAttrs, String sessionID,
			StoreContext storeContext) {
		  for (Map.Entry<String, Object> entry : modifiedAttrs.entrySet()) {
	            String attrName = entry.getKey();
	            Object attrValue = entry.getValue();

	            if (attrValue == null) {
	                cache.remove(attrName);
	            } else {
	                cache.put(sessionID,attrName, attrValue);
	            }
	        }
		
	}

}
