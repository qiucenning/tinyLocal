/**
 * Copyright (c) 1997-2013, www.tinygroup.org (luo_guo@icloud.com).
 * <p/>
 * Licensed under the GPL, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.gnu.org/licenses/gpl.html
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tinygroup.tinysqldsl.base;

import org.tinygroup.tinysqldsl.expression.Expression;
import org.tinygroup.tinysqldsl.visitor.ExpressionVisitor;

/**
 * 条件
 * Created by luoguo on 2015/3/11.
 */
public class Condition implements Expression {
    /**
     * 表达式
     */
    private Expression expression;
    /**
     * 条件中的值
     */
    private Object[] values;

    public Condition(Expression expression, Object... values) {
        super();
        this.expression = expression;
        this.values = values;
    }

    public Expression getExpression() {
        return expression;
    }

    public Object[] getValues() {
        return values;
    }

    public String toString() {
        return expression.toString();
    }

    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

}