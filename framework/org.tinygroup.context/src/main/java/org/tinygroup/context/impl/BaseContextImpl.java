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
package org.tinygroup.context.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.tinygroup.context.BaseContext;

@SuppressWarnings("unchecked")
public class BaseContextImpl implements BaseContext,Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2965722554039526665L;
	protected Map<String, Object> itemMap = new HashMap<String, Object>();

	public <T> T put(final String name, final T object) {
		return (T) itemMap.put(name, object);
	}

	public <T> T remove(final String name) {
		return (T) itemMap.remove(name);
	}

	public <T> T get(final String name) {
		return (T) itemMap.get(name);
	}

	public <T> T get(final String name, final T defaultValue) {
		T result = (T) itemMap.get(name);
		if (result == null) {
			result = defaultValue;
		}
		return result;
	}

	public boolean exist(String name) {
		return itemMap.containsKey(name);
	}

	public void clear() {
		itemMap.clear();
	}

	public Map<String, Object> getItemMap() {
		return itemMap;
	}

	public int size() {
		return itemMap.size();
	}

	public boolean renameKey(String key, String newKey) {
		if (itemMap.containsKey(key)) {
			itemMap.put(newKey, itemMap.get(key));
			itemMap.remove(key);
			return true;
		}
		return false;
	}
}
