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
package org.tinygroup.tinydbdsl.expression.relational;

import org.tinygroup.tinydbdsl.expression.BinaryExpression;
import org.tinygroup.tinydbdsl.expression.Expression;
import org.tinygroup.tinydbdsl.visitor.ExpressionVisitor;

public class RegExpMySQLOperator extends BinaryExpression {

	private RegExpMatchOperatorType operatorType;

	public RegExpMySQLOperator(Expression leftExpression,
			Expression rightExpression, boolean not,
			RegExpMatchOperatorType operatorType) {
		super(leftExpression, rightExpression, not);
		if (operatorType == null) {
			throw new NullPointerException();
		}
		this.operatorType = operatorType;
	}

	public RegExpMatchOperatorType getOperatorType() {
		return operatorType;
	}

	public String getStringExpression() {
		switch (operatorType) {
		case MATCH_CASESENSITIVE:
			return "REGEXP BINARY";
		case MATCH_CASEINSENSITIVE:
			return "REGEXP";
		default:
		}
		return null;
	}

	public void accept(ExpressionVisitor expressionVisitor) {
		expressionVisitor.visit(this);
	}
}
