package org.tinygroup.tinydbdsl.visitor.impl;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinydbdsl.base.Column;
import org.tinygroup.tinydbdsl.base.SelectBody;
import org.tinygroup.tinydbdsl.expression.Expression;
import org.tinygroup.tinydbdsl.select.Join;
import org.tinygroup.tinydbdsl.update.UpdateBody;
import org.tinygroup.tinydbdsl.util.DslUtil;
import org.tinygroup.tinydbdsl.visitor.ExpressionVisitor;
import org.tinygroup.tinydbdsl.visitor.SelectVisitor;

/**
 * A class to de-parse (that is, tranform from JSqlParser hierarchy into a
 * string) an {@link org.tinygroup.jsqlparser.statement.update.Update}
 */
public class UpdateDeParser {

	private StringBuilder buffer;
	private ExpressionVisitor expressionVisitor;
	private SelectVisitor selectVisitor;
	private List<Object> values = new ArrayList<Object>();

	/**
	 * @param expressionVisitor
	 *            a {@link ExpressionVisitor} to de-parse expressions. It has to
	 *            share the same<br>
	 *            StringBuilder (buffer parameter) as this object in order to
	 *            work
	 * @param selectVisitor
	 *            a {@link SelectVisitor} to de-parse
	 *            {@link org.tinygroup.jsqlparser.statement.select.Select}s. It
	 *            has to share the same<br>
	 *            StringBuilder (buffer parameter) as this object in order to
	 *            work
	 * @param buffer
	 *            the buffer that will be filled with the select
	 */
	public UpdateDeParser(ExpressionVisitor expressionVisitor,
			SelectVisitor selectVisitor, StringBuilder buffer,
			List<Object> values) {
		this.buffer = buffer;
		this.expressionVisitor = expressionVisitor;
		this.selectVisitor = selectVisitor;
		this.values = values;
	}

	public StringBuilder getBuffer() {
		return buffer;
	}

	public void setBuffer(StringBuilder buffer) {
		this.buffer = buffer;
	}

	public List<Object> getValues() {
		return values;
	}

	public void setValues(List<Object> values) {
		this.values = values;
	}

	public void deParse(UpdateBody update) {
		buffer.append("UPDATE ")
				.append(DslUtil.getStringList(update.getTables(), true, false))
				.append(" SET ");

		if (!update.isUseSelect()) {
			for (int i = 0; i < update.getColumns().size(); i++) {
				Column column = update.getColumns().get(i);
				buffer.append(column.getFullyQualifiedName()).append(" = ");

				Expression expression = update.getExpressions().get(i);
				expression.accept(expressionVisitor);
				if (i < update.getColumns().size() - 1) {
					buffer.append(", ");
				}
			}
		} else {
			if (update.isUseColumnsBrackets()) {
				buffer.append("(");
			}
			for (int i = 0; i < update.getColumns().size(); i++) {
				if (i != 0) {
					buffer.append(", ");
				}
				Column column = update.getColumns().get(i);
				buffer.append(column.getFullyQualifiedName());
			}
			if (update.isUseColumnsBrackets()) {
				buffer.append(")");
			}
			buffer.append(" = ");
			buffer.append("(");
			SelectBody selectBody = update.getSelectBody();
			selectBody.accept(selectVisitor);
			buffer.append(")");
		}

		if (update.getFromItem() != null) {
			buffer.append(" FROM ").append(update.getFromItem());
			if (update.getJoins() != null) {
				for (Join join : update.getJoins()) {
					if (join.isSimple()) {
						buffer.append(", ").append(join);
					} else {
						buffer.append(" ").append(join);
					}
				}
			}
		}

		if (update.getWhere() != null) {
			buffer.append(" WHERE ");
			update.getWhere().accept(expressionVisitor);
		}

	}

	public ExpressionVisitor getExpressionVisitor() {
		return expressionVisitor;
	}

	public void setExpressionVisitor(ExpressionVisitor visitor) {
		expressionVisitor = visitor;
	}
}
