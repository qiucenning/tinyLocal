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
package org.tinygroup.database.table.impl;

import org.tinygroup.database.config.table.Index;
import org.tinygroup.database.config.table.Table;
import org.tinygroup.database.table.TableSqlProcessor;


public class SqlserverSqlProcessorImpl extends SqlProcessorImpl {

	private static TableSqlProcessor tableSqlProcessor=new SqlserverSqlProcessorImpl();
	
	public static TableSqlProcessor getTableSqlProcessor(){
		tableSqlProcessor.setTableProcessor(TableProcessorImpl.getTableProcessor());
		return tableSqlProcessor;
	}
	
	protected String getDatabaseType() {
		return "sqlserver";
	}

	protected String appendIncrease() {
		return " identity(1,1) ";
	}

	protected String createNotNullSql(String tableName, String fieldName,
			String tableDataType) {
		return String.format("ALTER TABLE %s ALTER COLUMN %s %s NOT NULL",
				tableName, fieldName,tableDataType);
	}

	protected String createNullSql(String tableName, String fieldName,
			String tableDataType) {
		return String.format("ALTER TABLE %s ALTER COLUMN %s %s NULL",
				tableName, fieldName,tableDataType);
	}

	protected String createAlterTypeSql(String tableName, String fieldName,
			String tableDataType) {
		return String.format("ALTER TABLE %s ALTER COLUMN %s %s", tableName,
				fieldName, tableDataType);
	}

    @Override
    protected String getIndexName(Index index, Table table) {
        return index.getName();
    }
}
