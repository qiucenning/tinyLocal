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
package org.tinygroup.jdbctemplatedslsession;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;
import org.tinygroup.commons.namestrategy.NameStrategy;
import org.tinygroup.commons.namestrategy.impl.CamelCaseStrategy;
import org.tinygroup.commons.tools.ArrayUtil;
import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.jdbctemplatedslsession.batch.BatchPreparedStatementSetterImpl;
import org.tinygroup.jdbctemplatedslsession.batch.InsertBatchOperate;
import org.tinygroup.jdbctemplatedslsession.exception.DslRuntimeException;
import org.tinygroup.jdbctemplatedslsession.extractor.PageResultSetExtractor;
import org.tinygroup.jdbctemplatedslsession.pageprocess.SimplePageSqlProcessSelector;
import org.tinygroup.jdbctemplatedslsession.provider.DefaultTableMetaDataProvider;
import org.tinygroup.jdbctemplatedslsession.rowmapper.SimpleRowMapperSelector;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.tinysqldsl.ComplexSelect;
import org.tinygroup.tinysqldsl.Delete;
import org.tinygroup.tinysqldsl.DslSession;
import org.tinygroup.tinysqldsl.Insert;
import org.tinygroup.tinysqldsl.Pager;
import org.tinygroup.tinysqldsl.Select;
import org.tinygroup.tinysqldsl.Update;
import org.tinygroup.tinysqldsl.base.InsertContext;

/**
 * DslSqlSession接口的jdbctemplate版实现
 * 
 * @author renhui
 * 
 */
public class SimpleDslSession implements DslSession {

	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcTemplate simpleJdbcTemplate;
	private TableMetaDataProvider provider;
	private DataFieldMaxValueIncrementer incrementer;
	private RowMapperSelector selector = new SimpleRowMapperSelector();
	private PageSqlProcessSelector pageSelector = new SimplePageSqlProcessSelector();
	private String dbType;
	private NameStrategy nameStrategy = new CamelCaseStrategy();
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SimpleDslSession.class);

	public SimpleDslSession(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		simpleJdbcTemplate = new SimpleJdbcTemplate(jdbcTemplate);
		provider = new DefaultTableMetaDataProvider();
		dbType = provider.getDbType(dataSource);
	}

	public SimpleDslSession(DataSource dataSource,
			DataFieldMaxValueIncrementer incrementer) {
		this(dataSource);
		this.incrementer = incrementer;
	}

	public RowMapperSelector getSelector() {
		return selector;
	}

	public DataFieldMaxValueIncrementer getIncrementer() {
		return incrementer;
	}

	public void setIncrementer(DataFieldMaxValueIncrementer incrementer) {
		this.incrementer = incrementer;
	}

	public void setSelector(RowMapperSelector selector) {
		this.selector = selector;
	}

	public PageSqlProcessSelector getPageSelector() {
		return pageSelector;
	}

	public void setPageSelector(PageSqlProcessSelector pageSelector) {
		this.pageSelector = pageSelector;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public int execute(Insert insert) {
		String sql = insert.parsedSql();
		logMessage(sql, insert.getValues());
		return jdbcTemplate.update(sql, insert.getValues().toArray());
	}

	@SuppressWarnings("unchecked")
	public <T> T executeAndReturnObject(Insert insert) {
		Class<T> pojoType = insert.getContext().getTable().getPojoType();
		return executeAndReturnObject(insert, pojoType);
	}

	public <T> T executeAndReturnObject(Insert insert, Class<T> clazz) {
		return executeAndReturnObject(insert, clazz, true);
	}

	public <T> T executeAndReturnObject(Insert insert, Class<T> clazz,
			boolean autoGeneratedKeys) {
		if (clazz == null) {
			throw new IllegalArgumentException(
					"The type argument can not be empty");
		}
		InsertContext context = insert.getContext();
		TableMetaData metaData = provider.generatedKeyNamesWithMetaData(
				jdbcTemplate.getDataSource(), context.getSchema(), null,
				context.getTableName());
		ObjectMapper mapper = new ObjectMapper(clazz);
		mapper.setDslSession(this);
		return mapper.assemble(autoGeneratedKeys, metaData, insert);
	}

	private void logMessage(String sql, List<Object> values) {
		LOGGER.logMessage(LogLevel.DEBUG, "Executing SQL:[{0}],values:{1}",
				sql, values);
	}

	public int execute(Update update) {
		String sql = update.parsedSql();
		logMessage(sql, update.getValues());
		return jdbcTemplate.update(sql, update.getValues().toArray());
	}

	public int execute(Delete delete) {
		String sql = delete.parsedSql();
		logMessage(sql, delete.getValues());
		return jdbcTemplate.update(sql, delete.getValues().toArray());
	}

	@SuppressWarnings("unchecked")
	public <T> T fetchOneResult(Select select, Class<T> requiredType) {
		String sql = select.parsedSql();
		logMessage(sql, select.getValues());
		return (T) jdbcTemplate.queryForObject(sql, select.getValues()
				.toArray(), selector.rowMapperSelector(requiredType));
	}

	@SuppressWarnings("unchecked")
	public <T> T[] fetchArray(Select select, Class<T> requiredType) {
		List<T> records = fetchList(select, requiredType);
		if (!CollectionUtil.isEmpty(records)) {
			return (T[]) records.toArray();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> fetchList(Select select, Class<T> requiredType) {
		String sql = select.parsedSql();
		logMessage(sql, select.getValues());
		return jdbcTemplate.query(sql, select.getValues().toArray(),
				selector.rowMapperSelector(requiredType));
	}

	@SuppressWarnings("unchecked")
	public <T> T[] fetchArray(ComplexSelect complexSelect, Class<T> requiredType) {
		List<T> records = fetchList(complexSelect, requiredType);
		if (!CollectionUtil.isEmpty(records)) {
			return (T[]) records.toArray();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> fetchList(ComplexSelect complexSelect,
			Class<T> requiredType) {
		String sql = complexSelect.parsedSql();
		logMessage(sql, complexSelect.getValues());
		return jdbcTemplate.query(sql, complexSelect.getValues().toArray(),
				selector.rowMapperSelector(requiredType));
	}

	@SuppressWarnings("unchecked")
	public <T> T fetchOneResult(ComplexSelect complexSelect,
			Class<T> requiredType) {
		String sql = complexSelect.parsedSql();
		logMessage(sql, complexSelect.getValues());
		return (T) jdbcTemplate.queryForObject(sql, complexSelect.getValues()
				.toArray(), selector.rowMapperSelector(requiredType));
	}

	@SuppressWarnings("unchecked")
	public <T> Pager<T> fetchPage(Select pageSelect, int start, int limit,
			boolean isCursor, Class<T> requiredType) {
		int totalCount = count(pageSelect);
		String processSql = pageSelect.parsedSql();
		if (!isCursor) {
			PageSqlMatchProcess process = pageSelector
					.pageSqlProcessSelect(dbType);
			Select select = pageSelect.copy();// 复制新的查询对象
			processSql = process.sqlProcess(select, start, limit);
		}
		logMessage(processSql, pageSelect.getValues());
		List<T> records = (List<T>) jdbcTemplate
				.query(processSql, ArrayUtil.toArray(pageSelect.getValues()),
						new PageResultSetExtractor(start, limit, isCursor,
								requiredType));
		return new Pager(totalCount, start, limit, records);
	}

	public <T> Pager<T> fetchCursorPage(Select pageSelect, int start,
			int limit, Class<T> requiredType) {
		return fetchPage(pageSelect, start, limit, true, requiredType);
	}

	public <T> Pager<T> fetchDialectPage(Select pageSelect, int start,
			int limit, Class<T> requiredType) {
		return fetchPage(pageSelect, start, limit, false, requiredType);
	}

	public int count(Select select) {
		String countSql = getCountSql(select.parsedSql());
		logMessage(countSql, select.getValues());
		return jdbcTemplate.queryForInt(countSql,
				ArrayUtil.toArray(select.getValues()));
	}

	private String getCountSql(String sql) {
		StringBuffer sb = new StringBuffer(" select count(0) from (");
		sb.append(sql).append(") temp");
		return sb.toString();
	}

	public int[] batchInsert(Insert insert, List<Map<String, Object>> params) {
		return batchInsert(insert, params, Integer.MAX_VALUE);
	}

	public int[] batchInsert(Insert insert, List<Map<String, Object>> params,
			int batchSize) {
		return batchInsert(insert, params, batchSize, true);
	}

	public int[] batchInsert(Insert insert, List<Map<String, Object>> params,
			int batchSize, boolean autoGeneratedKeys) {
		final List<Integer> records = new ArrayList<Integer>();
		final InsertBatchOperate insertBatchOperate = newInsertBatch(insert,
				autoGeneratedKeys);
		if (params.size() > batchSize) {
			batchProcess(batchSize, params, new BatchOperateCallback() {
				public int[] callback(List<Map<String, Object>> params) {
					int[] affecctNums = insertBatchOperate.batchProcess(params);
					Collections.addAll(records,
							ArrayUtils.toObject(affecctNums));
					return affecctNums;
				}

				public int[] callbackList(List<List<Object>> params) {
					return null;
				}

				public int[] callback(Map<String, Object>[] params) {
					return null;
				}
			});
			return ArrayUtils.toPrimitive(records.toArray(new Integer[0]));
		}
		return insertBatchOperate.batchProcess(params);
	}

	private InsertBatchOperate newInsertBatch(Insert insert,
			boolean autoGeneratedKeys) {
		TableMetaData metaData = provider.generatedKeyNamesWithMetaData(
				jdbcTemplate.getDataSource(), insert.getContext().getSchema(),
				null, insert.getContext().getTableName());
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
		return new InsertBatchOperate(autoGeneratedKeys, insert, metaData,
				simpleJdbcInsert, incrementer);
	}

	private void batchProcess(int batchSize, List<Map<String, Object>> params,
			BatchOperateCallback callback) {
		int totalSize = params.size();
		int times = totalSize % batchSize == 0 ? totalSize / batchSize
				: totalSize / batchSize + 1;
		int numOfEach = totalSize % times == 0 ? totalSize / times : totalSize
				/ times + 1;
		int fromIndex = 0;

		for (int i = 0; i < times; i++) {
			int endIndex = fromIndex + numOfEach;
			if (endIndex > totalSize) {
				endIndex = totalSize;
			}
			List<Map<String, Object>> processParams = params.subList(fromIndex,
					endIndex);
			fromIndex += numOfEach;
			callback.callback(processParams);
		}
	}

	public <T> int[] batchInsert(Insert insert, Class<T> requiredType,
			List<T> params) {
		return batchInsert(insert, requiredType, params, Integer.MAX_VALUE);
	}

	public <T> int[] batchInsert(Insert insert, Class<T> requiredType,
			List<T> params, int batchSize) {
		return batchInsert(insert, requiredType, params, Integer.MAX_VALUE,
				true);
	}

	public <T> int[] batchInsert(Insert insert, Class<T> requiredType,
			List<T> params, int batchSize, boolean autoGeneratedKeys) {
		List<Map<String, Object>> batchArgs = convertMap(params);
		return batchInsert(insert, batchArgs, batchSize, autoGeneratedKeys);
	}

	private <T> List<Map<String, Object>> convertMap(List<T> params) {
		List<Map<String, Object>> batchArgs = new ArrayList<Map<String, Object>>();
		for (T param : params) {
			try {
				batchArgs.add(convertMap(param));
			} catch (Exception e) {
				LOGGER.errorMessage("pojo对象转换Map出错", e);
				throw new DslRuntimeException(e);
			}
		}
		return batchArgs;
	}

	private <T> Map<String, Object>[] convertBeanToArray(List<T> params) {
		Map<String, Object>[] batchArgs = new Map[params.size()];
		for (int j = 0; j < params.size(); j++) {
			try {
				batchArgs[j] = convertMap(params.get(j));
			} catch (Exception e) {
				LOGGER.errorMessage("pojo对象转换Map出错", e);
				throw new DslRuntimeException(e);
			}
		}
		return batchArgs;
	}

	private <T> Map<String, Object> convertMap(T param) throws Exception {
		Map<String, Object> description = new HashMap<String, Object>();
		PropertyDescriptor[] descriptors = PropertyUtils
				.getPropertyDescriptors(param);
		for (int i = 0; i < descriptors.length; i++) {
			String name = descriptors[i].getName();
			if (PropertyUtils.isReadable(param, name)
					&& PropertyUtils.isWriteable(param, name)) {
				if (descriptors[i].getReadMethod() != null) {
					Object value = PropertyUtils.getProperty(param, name);
					description.put(name, value);
					description.put(nameStrategy.getFieldName(name), value);
				}
			}
		}
		return description;
	}

	public int[] batchUpdate(Update update, List<List<Object>> params) {
		return batchUpdate(update, params, Integer.MAX_VALUE);
	}

	public int[] batchUpdate(final Update update, List<List<Object>> params,
			int batchSize) {
		return executeBatch(update.sql(), params, batchSize);
	}

	private int[] executeBatch(final String sql, List<List<Object>> params,
			int batchSize) {

		final List<Integer> records = new ArrayList<Integer>();
		if (params.size() > batchSize) {
			executeBatchProcess(batchSize, params, new BatchOperateCallback() {

				public int[] callbackList(List<List<Object>> params) {
					int[] affecctNums = jdbcTemplate.batchUpdate(sql,
							new BatchPreparedStatementSetterImpl(params, null));
					Collections.addAll(records,
							ArrayUtils.toObject(affecctNums));
					return affecctNums;
				}

				public int[] callback(List<Map<String, Object>> params) {
					return null;
				}

				public int[] callback(Map<String, Object>[] params) {
					return null;
				}
			});
			return ArrayUtils.toPrimitive(records.toArray(new Integer[0]));
		}
		return jdbcTemplate.batchUpdate(sql,
				new BatchPreparedStatementSetterImpl(params, null));

	}

	private void executeBatchProcess(int batchSize, List<List<Object>> params,
			BatchOperateCallback callback) {
		int totalSize = params.size();
		int times = totalSize % batchSize == 0 ? totalSize / batchSize
				: totalSize / batchSize + 1;
		int numOfEach = totalSize % times == 0 ? totalSize / times : totalSize
				/ times + 1;
		int fromIndex = 0;

		for (int i = 0; i < times; i++) {
			int endIndex = fromIndex + numOfEach;
			if (endIndex > totalSize) {
				endIndex = totalSize;
			}
			List<List<Object>> processParams = params.subList(fromIndex,
					endIndex);
			fromIndex += numOfEach;
			callback.callbackList(processParams);
		}
	}

	public int[] batchDelete(Delete delete, List<List<Object>> params) {
		return batchDelete(delete, params, Integer.MAX_VALUE);
	}

	public int[] batchDelete(Delete delete, List<List<Object>> params,
			int batchSize) {
		return executeBatch(delete.sql(), params, batchSize);
	}

	public int[] batchUpdate(Update update, Map<String, Object>[] params) {
		return batchUpdate(update, params, Integer.MAX_VALUE);
	}

	public <T> int[] batchUpdate(Update update, Class<T> requiredType,
			List<T> params) {
		return batchUpdate(update, requiredType, params, Integer.MAX_VALUE);
	}

	public int[] batchUpdate(final Update update, Map<String, Object>[] params,
			int batchSize) {
		return executeBatchUpdate(update.sql(), params, batchSize);
	}

	private int[] executeBatchUpdate(final String sql,
			Map<String, Object>[] params, int batchSize) {
		final List<Integer> records = new ArrayList<Integer>();
		if (params.length > batchSize) {
			batchProcess(batchSize, params, new BatchOperateCallback() {
				public int[] callback(List<Map<String, Object>> params) {
					return null;
				}

				public int[] callbackList(List<List<Object>> params) {
					return null;
				}

				public int[] callback(Map<String, Object>[] params) {
					int[] affecctNums = simpleJdbcTemplate.batchUpdate(sql,
							params);
					Collections.addAll(records,
							ArrayUtils.toObject(affecctNums));
					return affecctNums;
				}
			});
			return ArrayUtils.toPrimitive(records.toArray(new Integer[0]));
		}
		return simpleJdbcTemplate.batchUpdate(sql, params);
	}

	private void batchProcess(int batchSize, Map<String, Object>[] params,
			BatchOperateCallback callback) {
		int totalSize = params.length;
		int times = totalSize % batchSize == 0 ? totalSize / batchSize
				: totalSize / batchSize + 1;
		int numOfEach = totalSize % times == 0 ? totalSize / times : totalSize
				/ times + 1;
		int fromIndex = 0;

		for (int i = 0; i < times; i++) {
			int endIndex = fromIndex + numOfEach;
			if (endIndex > totalSize) {
				endIndex = totalSize;
			}
			Map<String, Object>[] processParams = (Map<String, Object>[]) ArrayUtils
					.subarray(params, fromIndex, endIndex);
			fromIndex += numOfEach;
			callback.callback(processParams);
		}
	}

	public <T> int[] batchUpdate(Update update, Class<T> requiredType,
			List<T> params, int batchSize) {
		Map<String, Object>[] mapParams = convertBeanToArray(params);
		return batchUpdate(update, mapParams, batchSize);
	}

	public int[] batchDelete(Delete delete, Map<String, Object>[] params) {
		return batchDelete(delete, params, Integer.MAX_VALUE);
	}

	public <T> int[] batchDelete(Delete delete, Class<T> requiredType,
			List<T> params) {
		return batchDelete(delete, requiredType, params, Integer.MAX_VALUE);
	}

	public int[] batchDelete(Delete delete, Map<String, Object>[] params,
			int batchSize) {
		return executeBatchUpdate(delete.sql(), params, batchSize);
	}

	public <T> int[] batchDelete(Delete delete, Class<T> requiredType,
			List<T> params, int batchSize) {
		Map<String, Object>[] mapParams = convertBeanToArray(params);
		return batchDelete(delete, mapParams, batchSize);
	}
}
