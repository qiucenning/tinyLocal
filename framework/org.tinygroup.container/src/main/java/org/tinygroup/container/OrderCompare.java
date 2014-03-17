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
package org.tinygroup.container;

import java.util.Comparator;

/**
 * 排序比较器<br>
 * 对基础类型进行排序比较
 * 
 * @author luoguo
 * @param <K>
 *            主键类型
 * @param <T>对象类型
 */
public class OrderCompare<K extends Comparable<K>, T extends BaseObject<K>>
		implements Comparator<T> {
	/**
	 * 首先按排序号排序，接下来按显示名排序
	 */
	public int compare(T object1, T object2) {
		if (object1.getOrder() > object2.getOrder()) {
			return 1;
		}
		if (object1.getOrder() < object2.getOrder()) {
			return -1;
		}
		return object1.getTitle().compareTo(object2.getTitle());
	}

}
