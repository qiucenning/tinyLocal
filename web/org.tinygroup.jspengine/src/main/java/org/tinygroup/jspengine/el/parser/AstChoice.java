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
package org.tinygroup.jspengine.el.parser;

import org.tinygroup.jspengine.el.lang.EvaluationContext;

import javax.el.ELException;


/**
 * @author Jacob Hookom [jacob@hookom.net]
 * @version $Change: 181177 $$DateTime: 2001/06/26 08:45:09 $$Author: tcfujii $
 */
public final class AstChoice extends SimpleNode {
    public AstChoice(int id) {
        super(id);
    }

    public Class getType(EvaluationContext ctx)
            throws ELException {
        Object val = this.getValue(ctx);
        return (val != null) ? val.getClass() : null;
    }

    public Object getValue(EvaluationContext ctx)
            throws ELException {
        Object obj0 = this.children[0].getValue(ctx);
        Boolean b0 = coerceToBoolean(obj0);
        return this.children[((b0.booleanValue() ? 1 : 2))].getValue(ctx);
    }

    public boolean isReadOnly(EvaluationContext ctx)
            throws ELException {
        Object obj0 = this.children[0].getValue(ctx);
        Boolean b0 = coerceToBoolean(obj0);
        return this.children[((b0.booleanValue() ? 1 : 2))].isReadOnly(ctx);
    }

    public void setValue(EvaluationContext ctx, Object value)
            throws ELException {
        Object obj0 = this.children[0].getValue(ctx);
        Boolean b0 = coerceToBoolean(obj0);
        this.children[((b0.booleanValue()? 1: 2))].setValue(ctx, value);
    }

    public Object invoke(EvaluationContext ctx,
                         Class[] paramTypes,
                         Object[] paramValues)
            throws ELException {
        Object obj0 = this.children[0].getValue(ctx);
        Boolean b0 = coerceToBoolean(obj0);
        return this.children[((b0.booleanValue() ? 1 : 2))]
            .invoke(ctx, paramTypes, paramValues);
    }

}
