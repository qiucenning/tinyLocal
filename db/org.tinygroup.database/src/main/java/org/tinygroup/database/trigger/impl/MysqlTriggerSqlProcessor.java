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
package org.tinygroup.database.trigger.impl;

import org.tinygroup.database.config.trigger.Trigger;

public class MysqlTriggerSqlProcessor extends AbstractTriggerSqlProcessor {

	protected String getTriggerSql(Trigger trigger) {
		String sql="SELECT * FROM information_schema.TRIGGERS where trigger_name='"+trigger.getName()+"'";
		return sql;
	}

}
