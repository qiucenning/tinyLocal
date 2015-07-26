package org.tinygroup.template.interpret.context;

import org.tinygroup.template.TemplateContext;
import org.tinygroup.template.interpret.ContextProcessor;
import org.tinygroup.template.interpret.TemplateFromContext;
import org.tinygroup.template.impl.TemplateEngineDefault;
import org.tinygroup.template.interpret.TemplateInterpreter;
import org.tinygroup.template.parser.grammer.TinyTemplateParser;
import org.tinygroup.template.rumtime.O;

import java.io.Writer;

/**
 * Created by luog on 15/7/17.
 */
public class MathBitwiseProcessor implements ContextProcessor<TinyTemplateParser.Expr_math_binary_bitwiseContext> {


    public Class<TinyTemplateParser.Expr_math_binary_bitwiseContext> getType() {
        return TinyTemplateParser.Expr_math_binary_bitwiseContext.class;
    }

    public boolean processChildren() {
        return false;
    }


    public Object process(TemplateInterpreter interpreter, TemplateFromContext templateFromContext, TinyTemplateParser.Expr_math_binary_bitwiseContext parseTree, TemplateContext pageContext, TemplateContext context, TemplateEngineDefault engine, Writer writer) throws Exception {
        Object a = interpreter.interpretTree(engine, templateFromContext, parseTree.expression().get(0),pageContext, context, writer);
        Object b = interpreter.interpretTree(engine, templateFromContext, parseTree.expression().get(1), pageContext,context, writer);
        return O.e(parseTree.getChild(1).getText(), a, b);
    }
}