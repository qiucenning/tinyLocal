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
package org.tinygroup.tinydb.util;

import org.springframework.jdbc.core.PreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class PreparedStatementSetterMapSetter implements PreparedStatementSetter {

	private List<Object> parameters;
	private List<Integer> dataTypes;
	
	public PreparedStatementSetterMapSetter( List<Object> parameters,List<Integer> dataTypes){
		this.parameters = parameters;
		this.dataTypes = dataTypes;
	}
	
	private void dealParams(PreparedStatement ps,
			List<Object> parameters, List<Integer> dataTypes)
			throws SQLException {
		if (parameters != null) {// 20120326 增加非空判断
			for (int i = 0; i < parameters.size(); i++) {
				if (dataTypes.size() > i && dataTypes.get(i) != null)
					setParameter(ps, i + 1, parameters.get(i), dataTypes.get(i));
				else
					setParameter(ps, i + 1, parameters.get(i), null);
			}
		}
	}

	private void setParameter(PreparedStatement ps, int index, Object obj,
			Integer dateType) throws SQLException {
		if (obj == null) {
			ps.setObject(index, obj, java.sql.Types.VARCHAR);
		} else {
			if (obj instanceof Character || obj.getClass().equals(char.class)) {
				ps.setString(index, obj.toString());
			} else if (obj instanceof String
					|| obj.getClass().equals(String.class)) {
				ps.setString(index, obj.toString());
			} else if (obj instanceof byte[]
					|| obj.getClass().equals(byte[].class)) {
				ps.setBytes(index, (byte[]) obj);
			} else {
				if (dateType == null) {
					ps.setObject(index, obj);
				} else {
					ps.setObject(index, obj, dateType);
				}
			}
		}
	}

	public void setValues(PreparedStatement ps) throws SQLException {
		dealParams(ps, parameters, dataTypes);
	}

}
