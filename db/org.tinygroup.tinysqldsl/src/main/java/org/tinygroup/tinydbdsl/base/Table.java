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
package org.tinygroup.tinydbdsl.base;

import org.tinygroup.tinydbdsl.formitem.FromItem;
import org.tinygroup.tinydbdsl.visitor.FromItemVisitor;

/**
 * Created by luoguo on 2015/3/11.
 */
public class Table implements FromItem, MultiPartName {
	private String schemaName;
	private String name;

	private Alias alias;

	public Table() {
	}

	public Table(String name) {
		this.name = name;
	}

	public Table(String schemaName, String name) {
		this.schemaName = schemaName;
		this.name = name;
	}

	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String string) {
		schemaName = string;
	}

	public String getName() {
		return name;
	}

	public void setName(String string) {
		name = string;
	}

	public Alias getAlias() {
		return alias;
	}

	public void setAlias(Alias alias) {
		this.alias = alias;
	}

	public String getFullyQualifiedName() {
		String fqn = "";

		if (schemaName != null) {
			fqn += schemaName;
		}
		if (!(fqn == null || fqn.length() == 0)) {
			fqn += ".";
		}

		if (name != null) {
			fqn += name;
		}

		return fqn;
	}

	public String toString() {
		return getFullyQualifiedName()
				+ ((alias != null) ? alias.toString() : "");
	}

	public void accept(FromItemVisitor fromItemVisitor) {
		fromItemVisitor.visit(this);
	}

}
