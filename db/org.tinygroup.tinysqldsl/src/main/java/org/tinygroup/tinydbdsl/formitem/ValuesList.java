package org.tinygroup.tinydbdsl.formitem;

import java.util.Iterator;
import java.util.List;

import org.tinygroup.tinydbdsl.base.Alias;
import org.tinygroup.tinydbdsl.expression.relational.ExpressionList;
import org.tinygroup.tinydbdsl.expression.relational.MultiExpressionList;
import org.tinygroup.tinydbdsl.util.DslUtil;
import org.tinygroup.tinydbdsl.visitor.FromItemVisitor;

/**
 * This is a container for a values item within a select statement. It holds
 * some syntactical stuff that differs from values within an insert statement.
 *
 * @author toben
 */
public class ValuesList implements FromItem {

	private Alias alias;
	private MultiExpressionList multiExpressionList;
	private boolean noBrackets = false;
	private List<String> columnNames;

	public ValuesList() {
	}

	public ValuesList(MultiExpressionList multiExpressionList) {
		this.multiExpressionList = multiExpressionList;
	}

	public Alias getAlias() {
		return alias;
	}

	
	public void setAlias(Alias alias) {
		this.alias = alias;
	}


    public MultiExpressionList getMultiExpressionList() {
		return multiExpressionList;
	}

	public void setMultiExpressionList(MultiExpressionList multiExpressionList) {
		this.multiExpressionList = multiExpressionList;
	}

	public boolean isNoBrackets() {
		return noBrackets;
	}

	public void setNoBrackets(boolean noBrackets) {
		this.noBrackets = noBrackets;
	}

	
	public String toString() {
		StringBuilder b = new StringBuilder();

		b.append("(VALUES ");
		for (Iterator<ExpressionList> it = getMultiExpressionList().getExprList().iterator(); it.hasNext();) {
			b.append(DslUtil.getStringList(it.next().getExpressions(), true, !isNoBrackets()));
			if (it.hasNext()) {
				b.append(", ");
			}
		}
		b.append(")");
		if (alias != null) {
			b.append(alias.toString());

			if (columnNames != null) {
				b.append("(");
				for (Iterator<String> it = columnNames.iterator(); it.hasNext();) {
					b.append(it.next());
					if (it.hasNext()) {
						b.append(", ");
					}
				}
				b.append(")");
			}
		}
		return b.toString();
	}

	public List<String> getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(List<String> columnNames) {
		this.columnNames = columnNames;
	}

	public void accept(FromItemVisitor fromItemVisitor) {
		fromItemVisitor.visit(this);		
	}
}
