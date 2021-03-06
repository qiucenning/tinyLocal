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
package org.tinygroup.template.interpret.context;

import org.tinygroup.template.TemplateContext;
import org.tinygroup.template.impl.TemplateEngineDefault;
import org.tinygroup.template.interpret.ContextProcessor;
import org.tinygroup.template.interpret.ReturnException;
import org.tinygroup.template.interpret.TemplateFromContext;
import org.tinygroup.template.interpret.TemplateInterpreter;
import org.tinygroup.template.parser.grammer.TinyTemplateParser;

import java.io.OutputStream;

/**
 * Created by luog on 15/7/17.
 */
public class LayoutDefineProcessor implements ContextProcessor<TinyTemplateParser.Layout_directiveContext> {

    public Class<TinyTemplateParser.Layout_directiveContext> getType() {
        return TinyTemplateParser.Layout_directiveContext.class;
    }


    public Object process(TemplateInterpreter interpreter, TemplateFromContext templateFromContext, TinyTemplateParser.Layout_directiveContext parseTree, TemplateContext pageContext, TemplateContext context, TemplateEngineDefault engine, OutputStream outputStream, String fileName) throws Exception {
        String name = "$" + parseTree.IDENTIFIER().getSymbol().getText();
        try {
            if (context.exist(name)) {
                interpreter.interpretTree(engine, templateFromContext, (TinyTemplateParser.BlockContext) context.get(name), pageContext, context, outputStream, fileName);
            } else {
                interpreter.interpretTree(engine, templateFromContext, parseTree.block(), pageContext, context, outputStream, fileName);
            }
        } catch (ReturnException e) {
            //接收退出指令
        }
        return null;
    }
}