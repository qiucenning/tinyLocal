package org.tinygroup.template.interpret.context;

import org.tinygroup.template.TemplateContext;
import org.tinygroup.template.interpret.ContextProcessor;
import org.tinygroup.template.interpret.TemplateFromContext;
import org.tinygroup.template.impl.TemplateEngineDefault;
import org.tinygroup.template.interpret.TemplateInterpreter;
import org.tinygroup.template.parser.grammer.TinyTemplateParser;
import org.tinygroup.template.rumtime.U;

import java.io.Writer;

/**
 * Created by luog on 15/7/17.
 */
public class ElseIfProcessor implements ContextProcessor<TinyTemplateParser.Elseif_directiveContext> {


    public Class<TinyTemplateParser.Elseif_directiveContext> getType() {
        return TinyTemplateParser.Elseif_directiveContext.class;
    }

    public boolean processChildren() {
        return false;
    }

    public Object process(TemplateInterpreter interpreter, TemplateFromContext templateFromContext, TinyTemplateParser.Elseif_directiveContext parseTree, TemplateContext pageContext, TemplateContext context, TemplateEngineDefault engine, Writer writer) throws Exception {
        //如果条件成立
        if (U.b(interpreter.interpretTree(engine, templateFromContext, parseTree.expression(),pageContext, context, writer))) {
            interpreter.interpretTree(engine, templateFromContext, parseTree.block(),pageContext, context, writer);
            return true;
        }
        return false;
    }

}