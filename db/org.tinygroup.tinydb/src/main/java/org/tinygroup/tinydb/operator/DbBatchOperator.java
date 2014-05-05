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
package org.tinygroup.tinydb.operator;

import org.tinygroup.tinydb.Bean;

import java.util.Collection;

/**
 * DB相关的批量操作
 * 
 * @author luoguo
 * 
 */
public interface DbBatchOperator<KeyType> {
	// 下面是根据数组
	
	Bean[] batchInsert(Bean[] beans);
	/**
	 * 
	 * 可以分批次进行批处理操作
	 * @param beans 批处理的所有记录
	 * @param batchSize 一次批处理最大数量
	 * @return
	 */
    Bean[] batchInsert(Bean[] beans,int batchSize);

	int[] batchUpdate(Bean[] beans);
	
	void batchUpdate(Bean[] beans,int batchSize);

	int[] batchDelete(Bean[] beans);
	
	void batchDelete(Bean[] beans,int batchSize);

	int[] deleteById(KeyType[] beanIds);

	Bean[] getBeansById(KeyType[] beanIds);

	Bean[] getBeans(Bean bean);

	// 下面是根据集合
	Bean[] batchInsert(Collection<Bean> beans);
	Bean[] batchInsert(Collection<Bean> beans,int batchSize);
	
	int[] batchUpdate(Collection<Bean> beans);
	void batchUpdate(Collection<Bean> beans,int batchSize);
	
	int[] batchDelete(Collection<Bean> beans);
	void batchDelete(Collection<Bean> beans,int batchSize);
	
	int[] deleteById(Collection<KeyType> beanIds);

	Bean[] getBeansById(Collection<KeyType> beanIds);
	
	/**
	 * 
	 * 处理的bean类型可以不一样，非批处理实现方式
	 * @param beans
	 * @return
	 */
	Bean[] insertBean(Bean[] beans);
	
	int[] updateBean(Bean[] beans);
	
	int[] deleteBean(Bean[] beans);

}
