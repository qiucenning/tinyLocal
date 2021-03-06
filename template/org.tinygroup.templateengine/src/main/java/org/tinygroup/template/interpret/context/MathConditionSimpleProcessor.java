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
package org.tinygroup.template.interpret.context;

import org.tinygroup.template.TemplateContext;
import org.tinygroup.template.impl.TemplateEngineDefault;
import org.tinygroup.template.interpret.ContextProcessor;
import org.tinygroup.template.interpret.TemplateFromContext;
import org.tinygroup.template.interpret.TemplateInterpreter;
import org.tinygroup.template.parser.grammer.TinyTemplateParser;
import org.tinygroup.template.rumtime.TemplateUtil;

import java.io.OutputStream;

/**
 * Created by luog on 15/7/17.
 */
public class MathConditionSimpleProcessor implements ContextProcessor<TinyTemplateParser.Expr_simple_condition_ternaryContext> {


    public Class<TinyTemplateParser.Expr_simple_condition_ternaryContext> getType() {
        return TinyTemplateParser.Expr_simple_condition_ternaryContext.class;
    }

    public Object process(TemplateInterpreter interpreter, TemplateFromContext templateFromContext, TinyTemplateParser.Expr_simple_condition_ternaryContext parseTree, TemplateContext pageContext, TemplateContext context, TemplateEngineDefault engine, OutputStream outputStream, String fileName) throws Exception {
        Object object = interpreter.interpretTree(engine, templateFromContext, parseTree.expression(0),pageContext, context, outputStream,fileName);
        Object right = interpreter.interpretTree(engine, templateFromContext, parseTree.expression(1),pageContext, context, outputStream,fileName);
        return TemplateUtil.getBooleanValue(object) ? object : right;
    }
}

