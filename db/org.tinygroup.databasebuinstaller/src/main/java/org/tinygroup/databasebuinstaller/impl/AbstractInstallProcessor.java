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
package org.tinygroup.databasebuinstaller.impl;

import org.tinygroup.databasebuinstaller.DataSourceHolder;
import org.tinygroup.databasebuinstaller.InstallProcessor;
import org.tinygroup.exception.BaseRuntimeException;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * 
 * 功能说明:安装处理的抽象实现
 * 
 * 开发人员: renhui <br>
 * 开发时间: 2013-8-15 <br>
 * <br>
 */
public abstract class AbstractInstallProcessor implements InstallProcessor {

	protected Logger logger = LoggerFactory
			.getLogger(AbstractInstallProcessor.class);

	public int getOrder() {
		return 0;
	}

	public void process(String language) {
		DataSource dataSource = DataSourceHolder.getDataSource();
		Connection con = null;
		try {
			con = dataSource.getConnection();
			processWithConn(language,con);
		} catch (SQLException ex) {
			throw new BaseRuntimeException(ex);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	protected void processWithConn(String language,Connection con) throws SQLException {
		List<String> sqls = getDealSqls(language, con);
		execute(sqls, con);

	}
	
	private void execute(List<String> sqls, Connection con) throws SQLException {
		Statement statement = null;
		con.setAutoCommit(false);
		try {
			statement = con.createStatement();
			logger.logMessage(LogLevel.INFO, "开始执行sql,共{0}句sql", sqls.size());
			for (String sql : sqls) {
				logger.logMessage(LogLevel.INFO, "执行sql:{0}", sql);
				statement.execute(sql);
			}
			con.commit();
			logger.logMessage(LogLevel.INFO, "执行sql处理完成");
		} catch (SQLException ex) {
			con.rollback();
			logger.errorMessage("执行sql处理异常，已回滚!");
			throw ex;
		} finally {
			if (statement != null) {
				statement.close();
			}

		}
	}


}
