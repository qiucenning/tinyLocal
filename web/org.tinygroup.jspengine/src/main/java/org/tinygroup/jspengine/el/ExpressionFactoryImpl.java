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
package org.tinygroup.jspengine.el;

import org.tinygroup.jspengine.el.lang.ELSupport;
import org.tinygroup.jspengine.el.lang.ExpressionBuilder;
import org.tinygroup.jspengine.el.util.MessageFactory;

import javax.el.*;


/**
 * @see javax.el.ExpressionFactory
 * 
 * @author Jacob Hookom [jacob@hookom.net]
 * @version $Change: 181177 $$DateTime: 2001/06/26 08:45:09 $$Author: tcfujii $
 */
public class ExpressionFactoryImpl extends ExpressionFactory {

    /**
     * 
     */
    public ExpressionFactoryImpl() {
        super();
    }

    public Object coerceToType(Object obj, Class type) {
        Object ret;
        try {
            ret = ELSupport.coerceToType(obj, type);
        } catch (IllegalArgumentException ex) {
            throw new ELException(ex);
        }
        return ret;
    }

    public MethodExpression createMethodExpression(ELContext context,
            String expression, Class expectedReturnType,
            Class[] expectedParamTypes) {
        if (expectedParamTypes == null) {
            throw new NullPointerException(MessageFactory
                    .get("error.method.nullParms"));
        }
        ExpressionBuilder builder = new ExpressionBuilder(expression, context);
        return builder.createMethodExpression(expectedReturnType,
                expectedParamTypes);
    }

    public ValueExpression createValueExpression(ELContext context,
            String expression, Class expectedType) {
        if (expectedType == null) {
            throw new NullPointerException(MessageFactory
                    .get("error.value.expectedType"));
        }
        ExpressionBuilder builder = new ExpressionBuilder(expression, context);
        return builder.createValueExpression(expectedType);
    }

    public ValueExpression createValueExpression(Object instance,
            Class expectedType) {
        if (expectedType == null) {
            throw new NullPointerException(MessageFactory
                    .get("error.value.expectedType"));
        }
        return new ValueExpressionLiteral(instance, expectedType);
    }
}
