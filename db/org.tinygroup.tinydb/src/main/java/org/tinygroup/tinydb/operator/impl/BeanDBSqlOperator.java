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
import org.tinygroup.tinydb.Bean;
import org.tinygroup.tinydb.exception.DBRuntimeException;
import org.tinygroup.tinydb.operator.DBOperator;
import org.tinygroup.tinydb.order.OrderBean;
import org.tinygroup.tinydb.query.QueryBean;
import org.tinygroup.tinydb.select.SelectBean;
import org.tinygroup.tinydb.util.TinyDBUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanDBSqlOperator<KeyType> extends BeanDBBatchOperator<KeyType>
		implements DBOperator<KeyType> {
	
	public BeanDBSqlOperator(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate);
	}

	public Bean[] getBeans(String sql) {
		try {
			return TinyDBUtil.collectionToArray(queryBean(buildSqlFuction(sql)));
		} catch (SQLException e) {
			throw new DBRuntimeException(e);
		}
	}

	public Bean[] getPagedBeans(String sql, int start, int limit) {
		return getPagedBeans(buildSqlFuction(sql), start, limit, new HashMap<String, Object>());
	}

	public Bean[] getBeans(String sql, Map<String, Object> parameters) {
		List<Bean> beans = findBeansByMap(buildSqlFuction(sql), getBeanType(), getSchema(),parameters,
				new ArrayList<Integer>());
		for (Bean bean : beans) {
			processRelation(bean, relation, new QueryRelationCallBack());
		}
		return TinyDBUtil.collectionToArray(beans);
	}

	public Bean[] getPagedBeans(String sql, int start, int limit, Map<String, Object> parameters) {
		List<Bean> beans = findBeansByMapForPage(buildSqlFuction(sql), getBeanType(),getSchema(), start,
				limit, parameters, new ArrayList<Integer>());
		for (Bean bean : beans) {
			processRelation(bean, relation, new QueryRelationCallBack());
		}
		return TinyDBUtil.collectionToArray(beans);
	}

	public Bean[] getBeans(String sql, Object... parameters) {
		List<Bean> beans = findBeans(buildSqlFuction(sql), getBeanType(),getSchema(), parameters);
		for (Bean bean : beans) {
			processRelation(bean, relation, new QueryRelationCallBack());
		}
		return TinyDBUtil.collectionToArray(beans);
	}
	
	public Bean[] getBeans(String sql, List<Object> parameters) {
		List<Bean> beans = findBeansByList(buildSqlFuction(sql), getBeanType(),getSchema(), parameters);
		for (Bean bean : beans) {
			processRelation(bean, relation, new QueryRelationCallBack());
		}
		return TinyDBUtil.collectionToArray(beans);
	}

	public Bean[] getPagedBeans(String sql, int start, int limit, Object... parameters) {
		List<Object> params = new ArrayList<Object>();
		if (parameters != null) {
			for (int i = 0; i < parameters.length; i++) {
				params.add(parameters[i]);
			}
		}

		List<Bean> beans = findBeansByListForPage(buildSqlFuction(sql), getBeanType(), getSchema(),start,
				limit, params);
		for (Bean bean : beans) {
			processRelation(bean, relation, new QueryRelationCallBack());
		}
		return TinyDBUtil.collectionToArray(beans);
	}

	public  Bean getSingleValue(String sql) {
		    Bean bean= (Bean) queryObject(buildSqlFuction(sql), getBeanType() ,getSchema());
			processRelation(bean, relation, new QueryRelationCallBack());
			return bean;
	}

	public Bean getSingleValue(String sql, Map<String, Object> parameters) {
		Bean bean= (Bean) queryObjectByMap(buildSqlFuction(sql), getBeanType(),getSchema(), parameters, null);
		processRelation(bean, relation, new QueryRelationCallBack());
		return bean;
	}

	public  Bean getSingleValue(String sql, Object... parameters) {
		Bean bean= (Bean) queryObject(buildSqlFuction(sql), getBeanType(),getSchema(), parameters);
		processRelation(bean, relation, new QueryRelationCallBack());
        return bean;
	}

	public Bean getSingleValue(String sql, List<Object> parameters) {
		Bean bean= (Bean) queryObject(buildSqlFuction(sql), getBeanType(),getSchema(), parameters.toArray());
		processRelation(bean, relation, new QueryRelationCallBack());
        return bean;
	}
	
	private List<Bean> queryBean(String sql) throws SQLException {
		List<Object> params = new ArrayList<Object>();
		List<Bean> beans= findBeansByList(sql, getBeanType(),getSchema(), params);
		for (Bean bean : beans) {
			processRelation(bean, relation, new QueryRelationCallBack());
		}
		return beans;
	}

	public Bean[] getBeans(SelectBean[] selectBeans, QueryBean queryBean,
			OrderBean[] orderBeans) {
		List<Object> valueList=new ArrayList<Object>();
		String sql= this.generateSqlClause(selectBeans, queryBean, orderBeans, valueList);
		return getBeans(sql, TinyDBUtil.collectionToArray(valueList));
	}

	public Bean[] getBeans(SelectBean[] selectBeans, QueryBean queryBean,
			OrderBean[] orderBeans, int start, int limit) {
		List<Object> valueList=new ArrayList<Object>();
		String sql= this.generateSqlClause(selectBeans, queryBean, orderBeans, valueList);
		return getPagedBeans(sql, start, limit, TinyDBUtil.collectionToArray(valueList));
	}

	public  Bean getSingleValue(SelectBean[] selectBeans, QueryBean queryBean) {
		List<Object> valueList=new ArrayList<Object>();
		String sql=this.generateSqlClause(selectBeans, queryBean, null, valueList);
		Bean bean= (Bean)queryObject(sql, getBeanType(),getSchema(), valueList.toArray());
		processRelation(bean, relation, new QueryRelationCallBack());
		return bean;
	}

	public Bean[] getBeans(String selectClause, QueryBean queryBean,
			OrderBean[] orderBeans) {
		List<Object> valueList=new ArrayList<Object>();
		String sql= this.generateSqlClause(selectClause, queryBean, orderBeans, valueList);
		return  getBeans(sql, TinyDBUtil.collectionToArray(valueList));
	}

	public Bean[] getBeans(String selectClause, QueryBean queryBean,
			OrderBean[] orderBeans, int start, int limit) {
		List<Object> valueList=new ArrayList<Object>();
		String sql=this.generateSqlClause(selectClause, queryBean, orderBeans, valueList);
		return  getPagedBeans(sql, start, limit, TinyDBUtil.collectionToArray(valueList));
	}

	public  Bean getSingleValue(String selectClause, QueryBean queryBean) {
		List<Object> valueList=new ArrayList<Object>();
		String sql=this.generateSqlClause(selectClause, queryBean, null, valueList);
		Bean bean= (Bean) queryObject(sql, getBeanType(),getSchema(), valueList.toArray());
		processRelation(bean, relation, new QueryRelationCallBack());
		return bean;
	}
	
	/**
	 * 产生SQL语句
	 * 
	 * @param queryBean
	 * @param stringBuffer
	 *            用于存放SQL
	 * @param valueList
	 *            用于存放值列表
	 */
	public void generateQuerySqlClause(QueryBean queryBean,
			StringBuffer stringBuffer, List<Object> valueList) {
		if (queryBean.getPropertyName() != null) {
			appendQueryBeanSql(queryBean, stringBuffer);
			if (queryBean.hasValue()) {
				valueList.add(queryBean.getValue());
			}
		}
		if (queryBean.getQueryBeanList() != null
				&& queryBean.getQueryBeanList().size() > 0) {
			if (queryBean.getPropertyName() != null) {
				stringBuffer.append(" ").append(queryBean.getConnectMode())
						.append(" ");
			}
			if (queryBean.getQueryBeanList().size() > 1) {
				stringBuffer.append("(");
			}
			for (int i = 0; i < queryBean.getQueryBeanList().size(); i++) {
				QueryBean subBean = queryBean.getQueryBeanList().get(i);
				if (i > 0) {
					stringBuffer.append(" ").append(queryBean.getConnectMode())
							.append(" ");
				}
				generateQuerySqlClause(subBean, stringBuffer, valueList);
			}
			if (queryBean.getQueryBeanList().size() > 1) {
				stringBuffer.append(")");
			}
		}
	}

	private StringBuffer appendQueryBeanSql(QueryBean queryBean, StringBuffer sb) {
		return sb.append(
				beanDbNameConverter.propertyNameToDbFieldName(queryBean
						.getPropertyName())).append(queryBean.getQueryClause());
	}

	/**
	 * 
	 * 生成查询部分的sql片段
	 * 
	 * @param selectBeans
	 * @param stringBuffer
	 */
	public void generateSelectSqlClause(SelectBean[] selectBeans,
			StringBuffer stringBuffer) {
		if (selectBeans != null && selectBeans.length > 0) {
			for (int i = 0; i < selectBeans.length; i++) {
				SelectBean selectBean = selectBeans[i];
				stringBuffer.append(selectBean.getSelectClause());
				if (i < selectBeans.length - 1) {
					stringBuffer.append(",");
				}
			}
		} else {
			stringBuffer.append("*");
		}
	}
    /**
     * 
     * 生成order by 子句
     * @param orderBeans
     * @param stringBuffer
     */
	public void generateOrderSqlClause(OrderBean[] orderBeans,
			StringBuffer stringBuffer) {
		if (orderBeans != null && orderBeans.length > 0) {
			stringBuffer.append(" order by ");
			for (int i = 0; i < orderBeans.length; i++) {
				OrderBean orderBean = orderBeans[i];
				stringBuffer
						.append(beanDbNameConverter
								.propertyNameToDbFieldName(orderBean
										.getPropertyName())).append(" ")
						.append(orderBean.getOrderMode()).append(" ");
				if(i<orderBeans.length-1){
					stringBuffer.append(",");
				}
			}
		}
	}
	/**
	 * 
	 * 生成sql语句
	 * @param selectBeans
	 * @param queryBean
	 * @param orderBeans
	 * @param valueList
	 */
	public String generateSqlClause(SelectBean[] selectBeans,QueryBean queryBean,OrderBean[] orderBeans,List<Object> valueList){
		StringBuffer stringBuffer=new StringBuffer();
		stringBuffer.append("select ");
		generateSelectSqlClause(selectBeans, stringBuffer);
		stringBuffer.append(" from ").append(beanDbNameConverter.typeNameToDbTableName(getBeanType()));
		stringBuffer.append(" where ");
		generateQuerySqlClause(queryBean, stringBuffer, valueList);
		generateOrderSqlClause(orderBeans, stringBuffer);
		return stringBuffer.toString();
	}
	
	public String generateSqlClause(String selectClause,QueryBean queryBean,OrderBean[] orderBeans,List<Object> valueList){
		StringBuffer stringBuffer=new StringBuffer();
		stringBuffer.append("select ").append(selectClause);
		stringBuffer.append(" from ").append(beanDbNameConverter.typeNameToDbTableName(getBeanType()));
		stringBuffer.append(" where ");
		generateQuerySqlClause(queryBean, stringBuffer, valueList);
		generateOrderSqlClause(orderBeans, stringBuffer);
		return stringBuffer.toString();
	}

	public Bean[] getBeans(QueryBean queryBean, OrderBean[] orderBeans) {
		SelectBean[] selectBeans=new SelectBean[0];
		return getBeans(selectBeans, queryBean, orderBeans);
	}

	public Bean[] getBeans(QueryBean queryBean, OrderBean[] orderBeans,
			int start, int limit) {
		SelectBean[] selectBeans=new SelectBean[0];
		return getBeans(selectBeans, queryBean, orderBeans, start, limit);
	}

	public Bean getSingleValue(QueryBean queryBean) {
		SelectBean[] selectBeans=new SelectBean[0];
		return (Bean) getSingleValue(selectBeans, queryBean);
	}

	public int execute(String sql, Map<String, Object> parameters) {
		return  executeByMap(buildSqlFuction(sql), parameters, null);
	}

	public int execute(String sql, Object... parameters) {
		return executeByArray(buildSqlFuction(sql), parameters);
	}

	public int execute(String sql,List<Object> parameters){
		return executeByList(buildSqlFuction(sql), parameters, null);
	}
    
	private String buildSqlFuction(String sql){
		return dialect.buildSqlFuction(sql);
	}

	public int account(String sql, Object... parameters) {
		return queryForInt(buildSqlFuction(sql), parameters);
	}

	public int account(String sql, List<Object> parameters) {
		return queryForIntByList(buildSqlFuction(sql), parameters);
	}

	public int account(String sql, Map<String, Object> parameters) {
		return queryForIntByMap(buildSqlFuction(sql), parameters);
	}

	
}
