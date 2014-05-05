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
package org.tinygroup.dbrouterjdbc3.jdbc;

import org.tinygroup.dbrouter.config.Partition;
import org.tinygroup.dbrouter.config.Shard;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RealStatementExecutor {
	private Statement realStatement;
	private String executeSql;
	private String originalSql;
	private Shard shard;
	private Partition partition;
	private Logger logger = LoggerFactory.getLogger(TinyStatement.class);

	public RealStatementExecutor(Statement realStatement, String executeSql,
			String originalSql, Shard shard, Partition partition) {
		super();
		this.realStatement = realStatement;
		this.executeSql = executeSql;
		this.originalSql = originalSql;
		this.partition = partition;
		this.shard = shard;
	}

	public void addBatch() throws SQLException {
		realStatement.addBatch(executeSql);
	}

	public Statement getRealStatement() {
		return realStatement;
	}

	public String getExecuteSql() {
		return executeSql;
	}

	public String getOriginalSql() {
		return originalSql;
	}

	public Shard getShard() {
		return shard;
	}

	public Partition getPartition() {
		return partition;
	}

	public ResultSet executeQuery() throws SQLException {
		logger.logMessage(LogLevel.DEBUG, "{0}:{1}", shard.getId(), originalSql);
		if (realStatement instanceof PreparedStatement) {
			PreparedStatement prepared = (PreparedStatement) realStatement;
			return prepared.executeQuery();
		}
		logger.logMessage(LogLevel.DEBUG, "{0}:{1}", shard.getId(), executeSql);
		return realStatement.executeQuery(executeSql);

	}

	public int executeUpdate() throws SQLException {
		logger.logMessage(LogLevel.DEBUG, "{0}:{1}", shard.getId(), originalSql);
		if (realStatement instanceof PreparedStatement) {
			PreparedStatement prepared = (PreparedStatement) realStatement;
			return prepared.executeUpdate();
		}
		logger.logMessage(LogLevel.DEBUG, "{0}:{1}", shard.getId(), executeSql);
		return realStatement.executeUpdate(executeSql);
	}
}
