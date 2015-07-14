/**
 *  Copyright (c) 1997-2013, www.tinygroup.org (tinygroup@126.com).
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
package org.tinygroup.jsqlparser.statement.select;

import java.io.Serializable;

/**
 * An offset clause in the form OFFSET offset
 * or in the form OFFSET offset (ROW | ROWS)
 */
public class Offset implements Serializable{

	private long offset;
	private boolean offsetJdbcParameter = false;
	private String offsetParam = null;

	public long getOffset() {
		return offset;
	}

	public String getOffsetParam() {
		return offsetParam;
	}

	public void setOffset(long l) {
		offset = l;
	}

	public void setOffsetParam(String s) {
		offsetParam = s;
	}

	public boolean isOffsetJdbcParameter() {
		return offsetJdbcParameter;
	}

	public void setOffsetJdbcParameter(boolean b) {
		offsetJdbcParameter = b;
	}

	@Override
	public String toString() {
		return " OFFSET " + (offsetJdbcParameter ? "?" : offset) + (offsetParam != null ? " "+offsetParam : "");
	}
}
