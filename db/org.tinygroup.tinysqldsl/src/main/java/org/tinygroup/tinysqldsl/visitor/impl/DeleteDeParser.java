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
package org.tinygroup.tinysqldsl.visitor.impl;

import org.tinygroup.tinysqldsl.delete.DeleteBody;
import org.tinygroup.tinysqldsl.visitor.ExpressionVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * A class to de-parse (that is, tranform from JSqlParser hierarchy into a
 * string) a {@link org.tinygroup.jsqlparser.statement.delete.Delete}
 */
public class DeleteDeParser {

    private StringBuilder buffer;
    private ExpressionVisitor expressionVisitor;
    private List<Object> values = new ArrayList<Object>();

    /**
     * @param expressionVisitor
     *            a {@link ExpressionVisitor} to de-parse expressions. It has to
     *            share the same<br>
     *            StringBuilder (buffer parameter) as this object in order to
     *            work
     * @param buffer
     *            the buffer that will be filled with the select
     */
    public DeleteDeParser(ExpressionVisitor expressionVisitor,
                          StringBuilder buffer, List<Object> values) {
        this.buffer = buffer;
        this.expressionVisitor = expressionVisitor;
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

    public void deParse(DeleteBody delete) {
        buffer.append("DELETE FROM ").append(
                delete.getTable().getFullyQualifiedName());
        if (delete.getWhere() != null) {
            buffer.append(" WHERE ");
            delete.getWhere().accept(expressionVisitor);
        }

    }

    public ExpressionVisitor getExpressionVisitor() {
        return expressionVisitor;
    }

    public void setExpressionVisitor(ExpressionVisitor visitor) {
        expressionVisitor = visitor;
    }
}
