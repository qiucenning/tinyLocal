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
package org.tinygroup.jdbctemplatedslsession.pageprocess;

import org.tinygroup.jdbctemplatedslsession.PageSqlMatchProcess;
import org.tinygroup.tinysqldsl.Select;

/**
 * 
 * @author renhui
 *
 */
public abstract class AbstractPageSqlMatchProcess implements
		PageSqlMatchProcess {
	

	public boolean isMatch(String dbType) {
		return dbType.indexOf(dbType())!=-1;
	}

	public String sqlProcess(Select select, int start, int limit) {
		return internalSqlProcess(select, start, limit);
	}

	protected abstract String dbType();
	
	protected abstract String internalSqlProcess(Select select,int start, int limit);

}
