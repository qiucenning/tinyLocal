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
package org.tinygroup.tinydb.operator.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameterValue;
import org.tinygroup.tinydb.Bean;
import org.tinygroup.tinydb.config.ColumnConfiguration;
import org.tinygroup.tinydb.config.TableConfiguration;
import org.tinygroup.tinydb.exception.DBRuntimeException;
import org.tinygroup.tinydb.operator.DBOperator;
import org.tinygroup.tinydb.util.TinyDBUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

 abstract class BeanDBBatchOperator<KeyType> extends
		BeanDBSingleOperator<KeyType> implements DBOperator<KeyType> {

	public BeanDBBatchOperator(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate);
	}

	public Bean[] getBeans(Bean bean) {
		TableConfiguration table = TinyDBUtil.getTableConfigByBean(
				bean.getType(), getSchema());
		List<String> conditionColumns = new ArrayList<String>();
		List<Object> params = new ArrayList<Object>();
		//List<Integer> dataTypes = new ArrayList<Integer>();
		for (ColumnConfiguration column : table.getColumns()) {
			String columnsName = column.getColumnName();
			String propertyName = beanDbNameConverter
					.dbFieldNameToPropertyName(columnsName);
			if (bean.containsKey(propertyName)) {
				conditionColumns.add(columnsName);
				params.add(bean.get(propertyName));
				//dataTypes.add(column.getDataType());
			}
		}
		String sql = getQuerySqlAndParamKeys(bean.getType(),conditionColumns);
		List<Bean> beans = findBeansByList(sql, bean.getType(), getSchema(),
				params);
		if (beans == null || beans.size() == 0) {
			return null;
		}
		for (Bean queryBean : beans) {
			processRelation(queryBean, relation, new QueryRelationCallBack());
		}
		return TinyDBUtil.listToArray(beans);
	}

	public Bean[] batchInsert(Bean[] beans) {
		Bean[] insertBeans = insertTopBeans(beans);
		// 关联bean插入
		if(insertBeans!=null){
			for (Bean bean : insertBeans) {
				processRelation(bean, relation, new InsertRelationCallBack());
			}
		}
		return insertBeans;
	}

	private Bean[] insertTopBeans(Bean[] beans) {
		if (beans == null || beans.length == 0) {
			return null;
		}
		checkBeanType(beans);
		String sql=getInsertSql(beans[0]);
		List<SqlParameterValue[]> params=getInsertParams(beans);
		executeBatchBySqlParamterValues(sql, params);
		return beans;
	}

	private void checkBeanType(Bean[] beans) {
		String beanType=beans[0].getType();
		for (int i = 1; i < beans.length; i++) {
			Bean bean = beans[i];
			if(!beanType.equals(bean.getType())){
				throw new DBRuntimeException("tinydb.batchBeanTypeError");
			}
		}
	}

	public int[] batchUpdate(Bean[] beans) {
		int[] records= updateTopBeans(beans);
		if(beans!=null){
			for (Bean bean : beans) {
				processRelation(bean, relation, new UpdateRelationCallBack());
			}
		}
		return records;
	}

	private int[] updateTopBeans(Bean[] beans) {
		if (beans == null || beans.length == 0) {
			return null;
		}
		checkBeanType(beans);
		List<String> conditionFields = new ArrayList<String>();
		TableConfiguration table = TinyDBUtil.getTableConfigByBean(
				beans[0].getType(), getSchema());
		ColumnConfiguration pk = table.getPrimaryKey();
		conditionFields.add(pk.getColumnName());
		//所有的bean都采用根据第一个bean生成的sql来做处理
		String sql =getUpdateSql(beans[0], conditionFields);
		SqlParameterValue[] values=getSqlParamterValue(beans[0], conditionFields);
        List<SqlParameterValue[]> params=getParams(beans, values);
		return executeBatchBySqlParamterValues(sql, params);
	}



	

	public int[] batchDelete(Bean[] beans) {
		int[] records= deleteTopBeans(beans);
		if(beans!=null){
			for (Bean bean : beans) {
				processRelation(bean, relation, new DeleteRelationCallBack());
			}
		}
		return records;
	}

	private int[] deleteTopBeans(Bean[] beans) {
		if (beans == null || beans.length == 0) {
			return null;
		}
		checkBeanType(beans);
		List<String> conditionColumns=getColumnNames(beans[0]);
		String sql = getDeleteSql(beans[0].getType(), conditionColumns);
		SqlParameterValue[] values=getSqlParameterValues(beans[0]);
		List<SqlParameterValue[]> params=getParams(beans, values);
		return executeBatchBySqlParamterValues(sql, params);
	}

	public int[] deleteById(KeyType[] beanIds) {
		if (beanIds == null || beanIds.length == 0) {
			return null;
		}		
		List<SqlParameterValue[]> params=new ArrayList<SqlParameterValue[]>();
		TableConfiguration table = TinyDBUtil.getTableConfigByBean(
				getBeanType(), getSchema());
		String sql = getDeleteSqlByKey(getBeanType());
		for (KeyType beanId : beanIds) {
			SqlParameterValue value=createSqlParamter(beanId, table.getPrimaryKey());
			SqlParameterValue[] values=new SqlParameterValue[1];
			values[0]=value;
			params.add(values);
		}
		return executeBatchBySqlParamterValues(sql, params);
	}

	public Bean[] getBeansById(KeyType[] beanIds) {
		try {
			List<Bean> list = queryBean(getBeanType(), beanIds);
			for (Bean bean : list) {
				processRelation(bean, relation, new QueryRelationCallBack());
			}
			
			return TinyDBUtil.collectionToArray(list);
		} catch (SQLException e) {
			throw new DBRuntimeException(e);
		}
	}

	public Bean[] batchInsert(Collection<Bean> beans) {
		return batchInsert(TinyDBUtil.collectionToArray(beans));
	}
	public Bean[] batchInsert(Collection<Bean> beans, int batchSize) {
		return batchInsert(TinyDBUtil.collectionToArray(beans),batchSize);
	}
	public int[] batchUpdate(Collection<Bean> beans) {
		return batchUpdate(TinyDBUtil.collectionToArray(beans));
	}

	public void batchUpdate(Collection<Bean> beans, int batchSize) {
		batchUpdate(TinyDBUtil.collectionToArray(beans),batchSize);
	}
	
	public int[] batchDelete(Collection<Bean> beans) {
		return batchDelete(TinyDBUtil.collectionToArray(beans));
	}
	public void batchDelete(Collection<Bean> beans, int batchSize) {
		batchDelete(TinyDBUtil.collectionToArray(beans),batchSize);
	}
	@SuppressWarnings("unchecked")
	public int[] deleteById(Collection<KeyType> beanIds) {
		return deleteById((KeyType[]) beanIds.toArray());
	}

	public Bean[] getBeansById(Collection<KeyType> beanIds) {
		return getBeansById(TinyDBUtil.collectionToArray(beanIds));
	}

	private List<Bean> queryBean(String beanType, KeyType[] beanIds)
			throws SQLException {

		String sql = getQuerySql();
		List<Bean> list = new ArrayList<Bean>();
		List<Integer> dataTypes = new ArrayList<Integer>();
		dataTypes.add(TinyDBUtil.getTableConfigByBean(beanType, getSchema())
				.getPrimaryKey().getDataType());
		for (KeyType beanId : beanIds) {
			List<Object> params = new ArrayList<Object>();
			params.add(beanId);
			list.addAll(findBeansByList(sql, beanType, getSchema(), params));
		}
		return list;

	}

	public Bean[] batchInsert(Bean[] beans, int batchSize) {
		
		if(beans.length>batchSize){
		   
			batchProcess(batchSize, Arrays.asList(beans), new BatchCallBack() {
				
				public void callback(List<Bean> beans) {
					batchInsert(beans);
				}
			});
			
		}else{
			batchInsert(beans);
		}
		return beans;
		
	}

	public void batchUpdate(Bean[] beans, int batchSize) {
		if(beans.length>batchSize){
			batchProcess(batchSize, Arrays.asList(beans), new BatchCallBack() {
				public void callback(List<Bean> beans) {
					batchUpdate(beans);
				}
			});
			
		}else{
			 batchUpdate(beans);
		}
	}

	public void batchDelete(Bean[] beans, int batchSize) {
		if(beans.length>batchSize){
			batchProcess(batchSize, Arrays.asList(beans), new BatchCallBack() {
				public void callback(List<Bean> beans) {
					batchDelete(beans);
				}
			});
			
		}else{
			batchDelete(beans);
		}
	}

	private void batchProcess(int batchSize,List<Bean> beans,BatchCallBack callback){
		int totalSize=beans.size();
		int times=totalSize%batchSize==0?totalSize/batchSize:totalSize/batchSize+1;
		int numOfEach = totalSize % times == 0 ? totalSize/times : totalSize/times + 1;
		int fromIndex = 0;
		 for (int i = 0; i < times; i++) {
				int endIndex= fromIndex + numOfEach;
				if(endIndex>totalSize){
					endIndex=totalSize;
				}
				List<Bean> processBeans = beans.subList(fromIndex,
						endIndex);
				fromIndex += numOfEach;
				callback.callback(processBeans);
		 }	
		
	}
	interface BatchCallBack{
		void callback(List<Bean> beans);
	}
	
	public Bean[] insertBean(Bean[] beans) {
		for (Bean bean : beans) {
			insert(bean);
		}
		return beans;
	}

	public int[] updateBean(Bean[] beans) {
		int[] records=new int[beans.length];
		for (int i = 0; i < beans.length; i++) {
			records[i]=update(beans[i]);
		}
	   return records;
	}

	public int[] deleteBean(Bean[] beans) {
		int[] records=new int[beans.length];
		for (int i = 0; i < beans.length; i++) {
			records[i]=delete(beans[i]);
		}
	   return records;
	}

	
}
