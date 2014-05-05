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
package org.tinygroup.database.util;

import org.tinygroup.commons.namestrategy.NameStrategy;
import org.tinygroup.commons.namestrategy.impl.NormalCaseStrategy;
import org.tinygroup.database.config.table.Table;
import org.tinygroup.database.config.table.TableField;
import org.tinygroup.database.table.TableProcessor;
import org.tinygroup.metadata.config.stdfield.StandardField;
import org.tinygroup.metadata.util.MetadataUtil;
import org.tinygroup.springutil.SpringUtil;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataBaseUtil {
	public static String DATABASE_XSTREAM = "database";
	public static String INITDATA_XSTREAM = "initdata";
	public static String PROCESSOR_XSTREAM = "processor";
	public static String PROCESSORMANAGER_BEAN = "processorManager";
	public static String TABLEPROCESSOR_BEAN = "tableProcessor";
	public static String CUSTOMESQL_BEAN = "customeSqlProcessor";
	public static String FUNCTION_BEAN = "dialectFunctionProcessor";
	public static String INITDATA_BEAN = "initDataProcessor";
	public static String PROCEDURE_BEAN = "procedureProcessor";
	public static String VIEW_BEAN = "viewProcessor";
	
	/* 数据库类型 */
	public static final String DB_TYPE_ORACLE = "oracle";
	public static final String DB_TYPE_DB2 = "db2";
	public static final String DB_TYPE_MYSQL = "mysql";
	public static final String DB_TYPE_SQLSERVER = "sqlserver";
	public static final String DB_TYPE_INFORMIX = "informix";
	public static final String DB_TYPE_SYBASE = "sybase"; 
	public static final String DB_TYPE_DERBY = "derby"; 

	public static StandardField getStandardField(String tableFieldId,
			Table table) {
		for (TableField field : table.getFieldList()) {
			if (field.getId().equals(tableFieldId)) {
				return MetadataUtil
						.getStandardField(field.getStandardFieldId());
			}
		}
		return null;
	}

	public static Table getTableById(String id) {
		TableProcessor tableProcessor = SpringUtil.getBean(
				DataBaseUtil.TABLEPROCESSOR_BEAN);
		return tableProcessor.getTableById(id);
	}
	
	public static TableField getPrimaryField(Table table){
		for(TableField field:table.getFieldList()){
			if(field.getPrimary())
				return field;
		}
		throw new RuntimeException("表格"+table.getName()+"主键不存在");
	}

	public static NameStrategy getNameStrategy() {
		return new NormalCaseStrategy();
	}

	public static String getDataBaseName(String name) {
		return getNameStrategy().getFieldName(name);
	}

	public static String getSchema(Table table,DatabaseMetaData metadata) throws SQLException{
		String schema = table.getSchema();
		if(schema == null ||"".equals(schema)){
			schema = metadata.getUserName();
		}
		return schema;
	}
	
	public static void closeResultSet(ResultSet r){
		if (r != null) {
			try {
				r.close();
			} catch (SQLException e) {
			}
		}
	}
}
