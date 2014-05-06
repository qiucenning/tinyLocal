/*
 * #%L
 * JSQLParser library
 * %%
 * Copyright (C) 2004 - 2013 JSQLParser
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as 
 * published by the Free Software Foundation, either version 2.1 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-2.1.html>.
 * #L%
 */
package org.tinygroup.jsqlparser.statement.create.table;

import java.util.List;

import org.tinygroup.jsqlparser.schema.Table;
import org.tinygroup.jsqlparser.statement.Statement;
import org.tinygroup.jsqlparser.statement.StatementVisitor;
import org.tinygroup.jsqlparser.statement.select.PlainSelect;

/**
 * A "CREATE TABLE" statement
 */
public class CreateTable implements Statement {

	private Table table;
	private List<String> tableOptionsStrings;
	private List<ColumnDefinition> columnDefinitions;
	private List<Index> indexes;


	public void accept(StatementVisitor statementVisitor) {
		statementVisitor.visit(this);
	}

	/**
	 * The name of the table to be created
	 */
	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	/**
	 * A list of {@link ColumnDefinition}s of this table.
	 */
	public List<ColumnDefinition> getColumnDefinitions() {
		return columnDefinitions;
	}

	public void setColumnDefinitions(List<ColumnDefinition> list) {
		columnDefinitions = list;
	}

	/**
	 * A list of options (as simple strings) of this table definition, as
	 * ("TYPE", "=", "MYISAM")
	 */
	public List<?> getTableOptionsStrings() {
		return tableOptionsStrings;
	}

	public void setTableOptionsStrings(List<String> list) {
		tableOptionsStrings = list;
	}

	/**
	 * A list of {@link Index}es (for example "PRIMARY KEY") of this table.<br>
	 * Indexes created with column definitions (as in mycol INT PRIMARY KEY) are
	 * not inserted into this list.
	 */
	public List<Index> getIndexes() {
		return indexes;
	}

	public void setIndexes(List<Index> list) {
		indexes = list;
	}


	public String toString() {
		String sql = "";

		sql = "CREATE TABLE " + table + " (";

		sql += PlainSelect.getStringList(columnDefinitions, true, false);
		if (indexes != null && indexes.size() != 0) {
			sql += ", ";
			sql += PlainSelect.getStringList(indexes);
		}
		sql += ")";
		String options = PlainSelect.getStringList(tableOptionsStrings, false, false);
		if (options != null && options.length() > 0) {
			sql += " " + options;
		}

		return sql;
	}
}
