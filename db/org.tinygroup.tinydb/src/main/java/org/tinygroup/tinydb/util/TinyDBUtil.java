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
package org.tinygroup.tinydb.util;

import org.tinygroup.context.Context;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.tinydb.Bean;
import org.tinygroup.tinydb.BeanOperatorManager;
import org.tinygroup.tinydb.config.TableConfiguration;
import org.tinygroup.tinydb.operator.DBOperator;

import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

/**
 * 工具方法
 * 
 * @author luoguo
 * 
 */
public class TinyDBUtil {

	private TinyDBUtil() {
	}

	private static BeanOperatorManager manager;

	static {
		manager = SpringUtil.getBean(BeanOperatorManager.OPERATOR_MANAGER_BEAN);
	}

	public static String getSeqName(String param) {
		return "seq_" + param;
	}

	public static <T extends Object> T[] listToArray(List<T> list) {
		if (list == null||list.size()==0) {
			return null;
		}

		T[] array = (T[]) Array
				.newInstance(list.get(0).getClass(), list.size());
		int i = 0;
		for (Object obj : list) {
			array[i++] = (T) obj;
		}
		return array;
	}

	@SuppressWarnings("unchecked")
	public static <T extends Object> T[] collectionToArray(
			Collection<T> collection) {
		if (collection == null||collection.size()==0) {
			return null;
		}
		
		T[] array = (T[]) Array.newInstance(collection.iterator().next()
				.getClass(), collection.size());
		int i = 0;
		for (Object obj : collection) {
			array[i++] = (T) obj;
		}
		return array;
	}
	
	

	public static TableConfiguration getTableConfig(String tableName,String schame) {
		return manager.getTableConfiguration(tableName,schame);
	}

	public static DBOperator<?> getDBOperator(String schema,String beanType) {
		return manager.getDbOperator(schema,beanType);
	}

	public static TableConfiguration getTableConfigByBean(String beanType,String schame) {
		return manager.getTableConfigurationByBean(beanType,schame);
	}

	/**
	 * 获取表信息
	 * 
	 *            表schema
	 * @param columnType
	 *            表名
	 * @return 表信息
	 * @throws SQLException
	 */

	public static String getColumnJavaType(String columnType) {
		return null;
	}

	public static List<String> getBeanProperties(String beanType,String schame) {
		return manager.getBeanProperties(beanType,schame);
	}

	public static Bean getBeanInstance(String beanType,String schame) {
		Bean bean = new Bean(beanType);
		return bean;
	}

	public static Bean context2Bean(Context c, String beanType,String schame) {
		List<String> properties = getBeanProperties(beanType,schame);
		Bean bean = context2Bean(c, beanType, properties,schame);
		return bean;
	}
	
	public static Bean context2Bean(Context c, String beanType) {
		List<String> properties = getBeanProperties(beanType,null);
		Bean bean = context2Bean(c, beanType, properties,null);
		return bean;
	}

	public static Bean context2Bean(Context c, String beanType,
			List<String> properties,String schame) {
		Bean bean = getBeanInstance(beanType,schame);
		for (String property : properties) {
			bean.put(property, c.get(property));
		}
		return bean;
	}
}
