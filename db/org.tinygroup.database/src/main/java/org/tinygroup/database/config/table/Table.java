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
package org.tinygroup.database.config.table;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.tinygroup.metadata.config.BaseObject;

import java.util.ArrayList;
import java.util.List;

@XStreamAlias("table")
public class Table extends BaseObject {
	@XStreamAsAttribute
	private String schema;
	@XStreamImplicit
	private List<TableField> fieldList;
	@XStreamImplicit
	private List<Index> indexList;
	@XStreamAsAttribute
	private String packageName;

	public String getNameWithOutSchema() {
		return super.getName();
	}
	
	public String getName() {
		if (getSchema() == null || "".equals(getSchema()))
			return super.getName();
		return String.format("%s.%s", getSchema(), super.getName());
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public List<TableField> getFieldList() {
		if (fieldList == null)
			fieldList = new ArrayList<TableField>();
		return fieldList;
	}

	public void setFieldList(List<TableField> fieldList) {
		this.fieldList = fieldList;
	}

	public List<Index> getIndexList() {
		if (indexList == null)
			indexList = new ArrayList<Index>();
		return indexList;
	}

	public void setIndexList(List<Index> indexList) {
		this.indexList = indexList;
	}

}
