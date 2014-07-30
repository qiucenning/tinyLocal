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
package org.tinygroup.tinydb.test;

import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.SQLException;

import junit.framework.TestCase;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.tinydb.Bean;
import org.tinygroup.tinydb.BeanOperatorManager;
import org.tinygroup.tinydb.exception.TinyDbException;
import org.tinygroup.tinydb.operator.DBOperator;
import org.tinygroup.tinydb.util.DataSourceFactory;
import org.tinygroup.tinytestutil.AbstractTestUtil;
import org.tinygroup.tinytestutil.script.Resources;
import org.tinygroup.tinytestutil.script.ScriptRunner;

public abstract class BaseTest extends TestCase {
	protected static BeanOperatorManager manager;
	private static DBOperator<String> operator;
	protected static String ANIMAL = "animal";
	protected static String PEOPLE = "aPeople";
	protected static String BRANCH = "aBranch";
	String mainSchema = "opensource";
	private static boolean hasExcuted = false;

	public DBOperator<String> getOperator() {
		return operator;
	}

	public void setOperator(DBOperator<String> operator) {
		BaseTest.operator = operator;
	}

	@SuppressWarnings("unchecked")
	public void setUp() {
		if (!hasExcuted) {
			Connection conn = null;
			try {
				AbstractTestUtil.init(null, true);
				conn = DataSourceFactory.getConnection("dynamicDataSource",
						this.getClass().getClassLoader());
				ScriptRunner runner = new ScriptRunner(conn, false, false);
				// 设置字符集
				Resources.setCharset(Charset.forName("utf-8"));
				// 加载sql脚本并执行
				try {
					runner.runScript(Resources
							.getResourceAsReader("table_derby.sql"));
				} catch (Exception e) {
					// e.printStackTrace();
				}
				manager = BeanContainerFactory.getBeanContainer(
						this.getClass().getClassLoader()).getBean(
						"beanOperatorManager");
				// people_id
				// people_name
				// people_age
				// branch
				// registerBean();
				// registerBean();
				// manager.initBeansConfiguration();
				operator = (DBOperator<String>) manager
						.getDbOperator(mainSchema);
				hasExcuted = true;
			} catch (TinyDbException e) {

			} finally {
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						fail(e.getMessage());
					}
				}
			}

		}

	}
	
	protected Bean getBean(String id){
		return getBean(id,"testSql");
	}
	
	private Bean getBean(String id,String name){
		Bean bean = new Bean(ANIMAL);
		bean.setProperty("id",id);
		bean.setProperty("name",name);
		bean.setProperty("length","1234");
		return bean;
	}
	
	protected Bean[] getBeans(int length){
		Bean[] insertBeans = new Bean[length];
		for(int i = 0 ; i < length ; i++ ){
			insertBeans[i] = getBean(i+"");
		}
		return insertBeans;
	}
}
