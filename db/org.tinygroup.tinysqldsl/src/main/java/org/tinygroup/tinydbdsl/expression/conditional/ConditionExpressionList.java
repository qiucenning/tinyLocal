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
package org.tinygroup.tinydbdsl.expression.conditional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.tinygroup.tinydbdsl.expression.Expression;
import org.tinygroup.tinydbdsl.expression.relational.ExpressionList;
import org.tinygroup.tinydbdsl.util.DslUtil;
import org.tinygroup.tinydbdsl.visitor.ExpressionVisitor;

public class ConditionExpressionList implements Expression{
	private List<Expression> expressions;

	private String comma = ",";
	
	private boolean useBrackets=true;
	
	private boolean useComma=true;

	public ConditionExpressionList() {
		expressions = new ArrayList<Expression>();
	}

	public ConditionExpressionList(List<Expression> expressions) {
		this.expressions = expressions;
	}

	public List<Expression> getExpressions() {
		return expressions;
	}

	public String getComma() {
		return comma;
	}

	public void setComma(String comma) {
		this.comma = comma;
	}
	
	public boolean isUseBrackets() {
		return useBrackets;
	}

	public void setUseBrackets(boolean useBrackets) {
		this.useBrackets = useBrackets;
	}

	public boolean isUseComma() {
		return useComma;
	}

	public void setUseComma(boolean useComma) {
		this.useComma = useComma;
	}

	public void setExpressions(List<Expression> list) {
		expressions = list;
	}

	public void addExpression(Expression expression) {
		if (expressions == null) {
			expressions = new ArrayList<Expression>();
		}
		expressions.add(expression);
	}

	public static ExpressionList expressionList(Expression expr) {
		return new ExpressionList(Collections.singletonList(expr));
	}

	public String toString() {
		return DslUtil.getStringList(expressions, useComma, useBrackets, comma);
	}

	public void accept(ExpressionVisitor expressionVisitor) {
		expressionVisitor.visit(this);
	}

}
