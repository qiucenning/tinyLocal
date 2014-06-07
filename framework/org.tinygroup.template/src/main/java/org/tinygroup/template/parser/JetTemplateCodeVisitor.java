/**
 * jetbrick-template
 * http://subchen.github.io/jetbrick-template/
 *
 * Copyright 2010-2014 Guoqiang Chen. All rights reserved.
 * Email: subchen@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tinygroup.template.parser;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.commons.lang.StringEscapeUtils;
import org.tinygroup.template.parser.grammer.JetTemplateParser;
import org.tinygroup.template.parser.grammer.JetTemplateParserVisitor;

import java.util.List;
import java.util.Stack;

// Visitor 模式访问器，用来生成 Java 代码
public class JetTemplateCodeVisitor extends AbstractParseTreeVisitor<CodeBlock> implements JetTemplateParserVisitor<CodeBlock> {
    Stack<CodeBlock> codeBlocks = new Stack<CodeBlock>();
    Stack<CodeLet> codeLets = new Stack<CodeLet>();
    private CodeBlock initCodeBlock = null;
    private CodeBlock macroCodeBlock = null;

    public CodeBlock visitExpression_list(@NotNull JetTemplateParser.Expression_listContext ctx) {

        List<JetTemplateParser.ExpressionContext> expression_list = ctx.expression();
        int i = 0;
        for (JetTemplateParser.ExpressionContext expression : expression_list) {
            CodeLet exp = new CodeLet();
            pushCodeLet(exp);
            expression.accept(this);
            popCodeLet();
            if (i > 0) {
                peekCodeLet().code(",");
            }
            peekCodeLet().code(exp);
            i++;
        }
        return null;
    }

    public CodeBlock visitInvalid_directive(@NotNull JetTemplateParser.Invalid_directiveContext ctx) {
        return null;
    }

    public CodeBlock visitElse_directive(@NotNull JetTemplateParser.Else_directiveContext ctx) {
        CodeBlock elseBlock = new CodeBlock().subCode(new CodeLet().lineCode("}else{")).tabIndent(-1);
        peekCodeBlock().subCode(elseBlock);
        ctx.block().accept(this);
        return null;
    }

    public CodeBlock visitType(@NotNull JetTemplateParser.TypeContext ctx) {
        return null;
    }

    public CodeBlock visitExpr_hash_map(@NotNull JetTemplateParser.Expr_hash_mapContext ctx) {
        JetTemplateParser.Hash_map_entry_listContext hash_map_entry_list = ctx.hash_map_entry_list();
        if (hash_map_entry_list != null) {
            peekCodeLet().code("{").code(hash_map_entry_list.accept(this).toString()).code("}");
        }
        return null;
    }

    public CodeBlock visitContinue_directive(@NotNull JetTemplateParser.Continue_directiveContext ctx) {
        JetTemplateParser.ExpressionContext expression = ctx.expression();
        if (expression != null) {
            pushCodeLet();
            expression.accept(this);
            CodeBlock ifCodeBlock = new CodeBlock().header(peekCodeLet().codeBefore("if(U.b(").lineCode(")){")).footer(new CodeLet().lineCode("}"));
            popCodeLet();
            ifCodeBlock.subCode(new CodeLet().lineCode("return;"));
            peekCodeBlock().subCode(ifCodeBlock);
        } else {
            peekCodeBlock().subCode(new CodeLet().lineCode("return;"));
        }
        return null;
    }


    public CodeBlock visitExpr_field_access(@NotNull JetTemplateParser.Expr_field_accessContext ctx) {
        ctx.expression(0).accept(this);
        pushCodeLet();
        ctx.expression(1).accept(this);
        CodeLet exp=peekCodeLet();
        popCodeLet();
        peekCodeLet().codeBefore("U.p(").code(",").code(exp).code(")");
        return null;
    }

    public CodeBlock visitExpr_compare_condition(@NotNull JetTemplateParser.Expr_compare_conditionContext ctx) {
        CodeLet left = pushPeekCodeLet();
        ctx.expression(0).accept(this);
        popCodeLet();
        CodeLet right = pushPeekCodeLet();
        ctx.expression(1).accept(this);
        popCodeLet();
        String op = ctx.getChild(1).getText();
        peekCodeLet().code(left).code(op).code(right);
        return null;
    }


    public CodeBlock visitExpr_instanceof(@NotNull JetTemplateParser.Expr_instanceofContext ctx) {
        return null;
    }

    public CodeBlock visitExpr_function_call(@NotNull JetTemplateParser.Expr_function_callContext ctx) {
        //
        return null;
    }

    public CodeBlock visitType_array_suffix(@NotNull JetTemplateParser.Type_array_suffixContext ctx) {
        return null;
    }

    public CodeBlock visitMacro_directive(@NotNull JetTemplateParser.Macro_directiveContext ctx) {
        String name = ctx.getChild(0).getText();
        name = name.substring(6, name.length() - 1).trim();
        initCodeBlock.subCode(new CodeLet().lineCode("getMacroMap().put(\"%s\", new %s());", name, name));
        //TODO
        CodeBlock macro = new CodeBlock();
        JetTemplateParser.Define_expression_listContext define_expression_list = ctx.define_expression_list();
        pushPeekCodeLet();
        if (define_expression_list != null) {
            define_expression_list.accept(this);
        }
        macro.header(new CodeLet().lineCode("class %s extends AbstractMacro {", name));
        macro.footer(new CodeLet().lineCode("}"));
        macro.subCode(constructMethod(name));
        popCodeLet();
        CodeBlock render = getMacroRenderCodeBlock();
        pushCodeBlock(render);
        macro.subCode(render);
        ctx.block().accept(this);
        popCodeBlock();
        macroCodeBlock.subCode(macro);
        return null;
    }

    private CodeBlock constructMethod(String name) {
        CodeBlock block = new CodeBlock();
        block.header(CodeLet.lineCodeLet("public %s() {", name));
        block.subCode("String[] args = {" + peekCodeLet() + "};");
        block.subCode("init(\"%s\", args);");
        block.footer(CodeLet.lineCodeLet("}"));
        return block;
    }

    public CodeBlock visitExpr_compare_equality(@NotNull JetTemplateParser.Expr_compare_equalityContext ctx) {
        CodeLet left = pushPeekCodeLet();
        ctx.expression(0).accept(this);
        popCodeLet();
        CodeLet right =pushPeekCodeLet();
        ctx.expression(1).accept(this);
        popCodeLet();
        String op = ctx.getChild(1).getText();
        peekCodeLet().code(left).code(op).code(right);
        return null;
    }

    public CodeBlock visitValue(@NotNull JetTemplateParser.ValueContext ctx) {

        pushCodeLet();
        ctx.expression().accept(this);
        Token token = ((TerminalNode) ctx.getChild(0)).getSymbol();
        if (token.getType() == JetTemplateParser.VALUE_ESCAPED_OPEN) {
            peekCodeLet().codeBefore("StringEscapeUtils.escapeHtml((").code(")+\"\")");
        }
        peekCodeLet().codeBefore("write($writer,").lineCode(");");
        CodeBlock valueCodeBlock = new CodeBlock();
        valueCodeBlock.subCode(peekCodeLet());
        popCodeLet();
        return valueCodeBlock;
    }

    public CodeBlock visitExpr_math_binary_bitwise(@NotNull JetTemplateParser.Expr_math_binary_bitwiseContext ctx) {
        peekCodeLet().code("O.e(\"").code(ctx.getChild(1).getText()).code("\",");
        ctx.expression(0).accept(this);
        peekCodeLet().code(",");
        ctx.expression(1).accept(this);
        peekCodeLet().code(")");
        return null;
    }


    public CodeBlock visitHash_map_entry_list(@NotNull JetTemplateParser.Hash_map_entry_listContext ctx) {
        List<JetTemplateParser.ExpressionContext> expression_list = ctx.expression();
        CodeLet keyPair = new CodeLet();
        CodeBlock result = new CodeBlock().subCode(keyPair);
        for (int i = 0; i < expression_list.size(); i += 2) {
            CodeBlock codeBlock = new CodeBlock();
            CodeLet keyCodeLet = pushPeekCodeLet();
            expression_list.get(i).accept(this);
            popCodeLet();
            CodeLet valueCodeLet = pushPeekCodeLet();
            expression_list.get(i + 1).accept(this);
            popCodeLet();
            codeBlock.subCode(new CodeLet().code(keyCodeLet).code(":").code(valueCodeLet));
            if (i > 0) {
                keyPair.code(",");
            }
            keyPair.code(keyCodeLet).code(":").code(valueCodeLet);
        }
        return result;
    }

    public CodeBlock visitDirective(@NotNull JetTemplateParser.DirectiveContext ctx) {
        return ctx.getChild(0).accept(this);
    }

    public CodeBlock visitTemplate(@NotNull JetTemplateParser.TemplateContext ctx) {
        CodeBlock templateCodeBlock = getTemplateCodeBlock();
        CodeBlock classCodeBlock = getClassCodeBlock();
        templateCodeBlock.subCode(classCodeBlock);
        CodeBlock renderMethodCodeBlock = getRenderCodeBlock();
        classCodeBlock.subCode(renderMethodCodeBlock);
        CodeBlock getTemplatePathMethod = getTemplatePathMethodCodeBlock();
        classCodeBlock.subCode(getTemplatePathMethod);
        pushCodeBlock(renderMethodCodeBlock);
        renderMethodCodeBlock.subCode(ctx.block().accept(this));
        popCodeBlock();
        return templateCodeBlock;
    }

    private CodeBlock getRenderCodeBlock() {
        CodeBlock renderMethod = new CodeBlock();
        renderMethod.header(new CodeLet().lineCode("protected void renderTemplate(TemplateContext $context, Writer $writer) throws IOException, TemplateException{")).footer(new CodeLet().lineCode("}"));
        renderMethod.subCode("Macro $macro=null;");
        renderMethod.subCode("Template $template=this;");
        renderMethod.subCode("TemplateContext $newContext=null;");

        return renderMethod;
    }
    private CodeBlock getMacroRenderCodeBlock() {
        CodeBlock renderMethod = new CodeBlock();
        renderMethod.header(new CodeLet().lineCode("protected void renderTemplate(Template $template, TemplateContext $context, Writer $writer) throws IOException, TemplateException{")).footer(new CodeLet().lineCode("}"));
        renderMethod.subCode("Macro $macro=null;");
        renderMethod.subCode("TemplateContext $newContext=null;");

        return renderMethod;
    }

    private CodeBlock getTemplatePathMethodCodeBlock() {
        CodeBlock renderMethod = new CodeBlock();
        renderMethod.header(new CodeLet().lineCode("public String getPath(){")).footer(new CodeLet().lineCode("}"));
        renderMethod.subCode(new CodeLet().lineCode("return \"$TEMPLATE_PATH\";"));
        return renderMethod;
    }

    private CodeBlock getClassCodeBlock() {
        CodeBlock templateClass = new CodeBlock();
        initCodeBlock = new CodeBlock().header(new CodeLet("{").endLine()).footer(new CodeLet("}").endLine());
        templateClass.header(new CodeLet().lineCode("public class $TEMPLATE_CLASS_NAME extends AbstractTemplate{"));
        templateClass.subCode(initCodeBlock);
        macroCodeBlock = new CodeBlock();
        templateClass.subCode(macroCodeBlock);
        templateClass.footer(new CodeLet().lineCode("}"));
        return templateClass;
    }

    private CodeBlock getTemplateCodeBlock() {
        CodeBlock codeBlock = new CodeBlock();
        codeBlock.subCode(new CodeLet().lineCode("import java.io.IOException;"));
        codeBlock.subCode(new CodeLet().lineCode("import org.tinygroup.template.rumtime.*;"));
        codeBlock.subCode(new CodeLet().lineCode("import org.tinygroup.template.*;"));
        codeBlock.subCode(new CodeLet().lineCode("import java.io.Writer;"));
        codeBlock.subCode(new CodeLet().lineCode("import org.tinygroup.template.impl.*;"));
        return codeBlock;
    }

    public CodeBlock visitExpr_compare_not(@NotNull JetTemplateParser.Expr_compare_notContext ctx) {
        return null;
    }

    public CodeBlock visitText(@NotNull JetTemplateParser.TextContext ctx) {
        Token token = ((TerminalNode) ctx.getChild(0)).getSymbol();
        String text = token.getText();
        switch (token.getType()) {
            case JetTemplateParser.COMMENT_LINE:
            case JetTemplateParser.COMMENT_BLOCK1:
            case JetTemplateParser.COMMENT_BLOCK2:
                return null;
            case JetTemplateParser.TEXT_PLAIN:
                break;
            case JetTemplateParser.TEXT_CDATA:
                text = text.substring(3, text.length() - 3).trim();
                break;
            case JetTemplateParser.TEXT_ESCAPED_CHAR:
                text = text.substring(1);
                break;
        }
        CodeBlock textCodeBlock = new CodeBlock().header(new CodeLet().code("write($writer,\"").code(StringEscapeUtils.escapeJava(text)).lineCode("\");"));
        return textCodeBlock;
    }

    public CodeBlock visitExpr_identifier(@NotNull JetTemplateParser.Expr_identifierContext ctx) {
        String name = ctx.IDENTIFIER().getText();
        if (name.startsWith("$")) {
            name = name.substring(1);
        }
        peekCodeLet().code("U.c($context,\"" + name + "\")");
        return null;
    }


    public CodeBlock visitIf_directive(@NotNull JetTemplateParser.If_directiveContext ctx) {
        CodeBlock ifCodeBlock = pushPeekCodeBlock();
        pushCodeLet();
        ctx.expression().accept(this);
        ifCodeBlock.header(peekCodeLet().codeBefore("if(U.b(").lineCode(")){"));
        popCodeLet();
        ifCodeBlock.footer(new CodeLet().lineCode("}"));
        ctx.block().accept(this);
        popCodeBlock();
        List<JetTemplateParser.Elseif_directiveContext> elseif_directive_list = ctx.elseif_directive();
        for (JetTemplateParser.Elseif_directiveContext elseif_directive : elseif_directive_list) {
            elseif_directive.accept(this);
        }
        JetTemplateParser.Else_directiveContext else_directive = ctx.else_directive();
        if (else_directive != null) {
            else_directive.accept(this);
        }
        return ifCodeBlock;
    }

    public CodeBlock visitExpr_math_unary_suffix(@NotNull JetTemplateParser.Expr_math_unary_suffixContext ctx) {
        return null;
    }

    public CodeBlock visitDefine_expression_list(@NotNull JetTemplateParser.Define_expression_listContext ctx) {
        if (peekCodeLet().length() > 0) {
            peekCodeLet().code(",");
        }
        peekCodeLet().code("\"%s\"", ctx.getChild(0).getText());
        return null;
    }


    public CodeBlock visitExpr_new_array(@NotNull JetTemplateParser.Expr_new_arrayContext ctx) {
        return null;
    }

    public CodeBlock visitCall_macro_directive(@NotNull JetTemplateParser.Call_macro_directiveContext ctx) {
        CodeBlock callMacro = new CodeBlock();
        String name = ctx.getChild(0).getText();
        name = name.substring(1, name.length());
        if(name.endsWith("(")){
            name=name.substring(0,name.length()-1);
        }
        callMacro.subCode(String.format("$macro=getTemplateEngine().findMacro(\"%s\",$template);", name));
        callMacro.subCode("$newContext = new TemplateContextImpl();");
        callMacro.subCode("$context.putSubContext(\"$newContext\",$newContext);");
        if (ctx.para_expression_list() != null) {
            List<JetTemplateParser.Para_expressionContext> expList = ctx.para_expression_list().para_expression();
            if (expList != null) {
                pushCodeBlock(callMacro);
                int i = 0;
                for (JetTemplateParser.Para_expressionContext visitPara_expression : expList) {
                    CodeLet expression = new CodeLet();
                    pushCodeLet(expression);
                    if (visitPara_expression.getChildCount() == 3) {//如果是带参数的
                        visitPara_expression.getChild(2).accept(this);
                        peekCodeBlock().subCode(String.format("$newContext.put(\"%s\",%s);", visitPara_expression.getChild(0).getText(), expression));
                    } else {
                        visitPara_expression.getChild(0).accept(this);
                        peekCodeBlock().subCode(String.format("$newContext.put($macro.getParameterNames()[%d],%s);", i, expression));
                    }
                    popCodeLet();
                    i++;
                }
                popCodeBlock();
            }
        }
        callMacro.subCode(String.format("getTemplateEngine().renderMacro(\"%s\", $template, $newContext, $writer);",name));
        callMacro.subCode("$context.removeSubContext(\"$newContext\");");
        return callMacro;
    }

    public CodeBlock visitType_name(@NotNull JetTemplateParser.Type_nameContext ctx) {
        return null;
    }

    public CodeBlock visitType_list(@NotNull JetTemplateParser.Type_listContext ctx) {
        return null;
    }

    public CodeBlock visitCall_macro_block_directive(@NotNull JetTemplateParser.Call_macro_block_directiveContext ctx) {
        CodeBlock callMacro = new CodeBlock();
        String name = ctx.getChild(0).getText();
        name = name.substring(2, name.length()-1).trim();
        callMacro.subCode(String.format("$macro=getTemplateEngine().findMacro(\"%s\",$template);", name));
        callMacro.subCode("$context.putSubContext(\"$newContext\",$newContext);");
        List<JetTemplateParser.Para_expressionContext> expList = ctx.para_expression_list().para_expression();
        if (expList != null) {
            pushCodeBlock(callMacro);
            int i = 0;
            for (JetTemplateParser.Para_expressionContext visitPara_expression : expList) {
                CodeLet expression = new CodeLet();
                pushCodeLet(expression);
                if (visitPara_expression.getChildCount() == 3) {//如果是带参数的
                    visitPara_expression.getChild(2).accept(this);
                    peekCodeBlock().subCode(String.format("$newContext.put(\"%s\",%s);", visitPara_expression.getChild(0).getText(), expression));
                } else {
                    visitPara_expression.getChild(0).accept(this);
                    peekCodeBlock().subCode(String.format("$newContext.put($macro.getParameterNames()[%d],%s);", i, expression));
                }
                popCodeLet();
                i++;
            }
            popCodeBlock();
        }
        CodeBlock bodyContentMacro = new CodeBlock();
        callMacro.subCode(bodyContentMacro);
        callMacro.subCode(String.format("getTemplateEngine().renderMacro(\"%s\", $template, $newContext, $writer);",name));

        bodyContentMacro.header("$newContext.put(\"$bodyContent\",new AbstractMacro() {");
        CodeBlock render = getMacroRenderCodeBlock();
        bodyContentMacro.subCode(render);

        pushCodeBlock(render);
        ctx.block().accept(this);
        popCodeBlock();
        bodyContentMacro.footer("});");
        callMacro.subCode("$context.removeSubContext(\"$newContext\");");

        //Body部分创建新的类，然后传入要调用的宏
        return callMacro;
    }

    public CodeBlock visitInclude_directive(@NotNull JetTemplateParser.Include_directiveContext ctx) {
        return null;
    }

    public CodeBlock visitPara_expression(@NotNull JetTemplateParser.Para_expressionContext ctx) {

        return null;
    }

    public CodeBlock visitExpr_array_get(@NotNull JetTemplateParser.Expr_array_getContext ctx) {
        ctx.expression(0).accept(this);
        peekCodeLet().codeBefore("U.a(").code(",");
        ctx.expression(1).accept(this);
        peekCodeLet().code(")");
        return null;
    }

    public CodeBlock visitBlock(@NotNull JetTemplateParser.BlockContext ctx) {
        for (int i = 0; i < ctx.getChildCount(); i++) {
            ParseTree node = ctx.children.get(i);
            CodeBlock codeBlock = node.accept(this);
            peekCodeBlock().subCode(codeBlock);
        }
        return null;
    }

    public CodeBlock visitExpr_compare_relational(@NotNull JetTemplateParser.Expr_compare_relationalContext ctx) {
        return null;
    }

    public CodeBlock visitType_arguments(@NotNull JetTemplateParser.Type_argumentsContext ctx) {
        return null;
    }


    public CodeBlock visitExpr_math_binary_basic(@NotNull JetTemplateParser.Expr_math_binary_basicContext ctx) {
        peekCodeLet().code("O.e(\"").code(ctx.getChild(1).getText()).code("\",");
        ctx.expression(0).accept(this);
        peekCodeLet().code(",");
        ctx.expression(1).accept(this);
        peekCodeLet().code(")");
        return null;
    }

    public CodeBlock visitPara_expression_list(@NotNull JetTemplateParser.Para_expression_listContext ctx) {

        return null;
    }

    public CodeBlock visitSet_expression(@NotNull JetTemplateParser.Set_expressionContext ctx) {
        CodeBlock codeBlock = new CodeBlock();
        CodeLet codeLet = pushPeekCodeLet();
        codeBlock.header(codeLet);
        ctx.getChild(2).accept(this);
        popCodeLet();
        codeLet.codeBefore("$context.put(\"" + ctx.getChild(0).getText() + "\",").lineCode(");");
        return codeBlock;
    }

    public CodeBlock visitTerminal(@org.antlr.v4.runtime.misc.NotNull org.antlr.v4.runtime.tree.TerminalNode node) {
        peekCodeLet().code(node.getText());
        return null;
    }

    public CodeBlock visitTag_directive(@NotNull JetTemplateParser.Tag_directiveContext ctx) {
        return null;
    }

    public CodeBlock visitSet_directive(@NotNull JetTemplateParser.Set_directiveContext ctx) {
        List<JetTemplateParser.Set_expressionContext> set_expression_list = ctx.set_expression();
        for (JetTemplateParser.Set_expressionContext node : set_expression_list) {
            peekCodeBlock().subCode(node.accept(this));
        }
        return null;
    }

    public CodeBlock visitExpr_new_object(@NotNull JetTemplateParser.Expr_new_objectContext ctx) {
        return null;
    }

    public CodeBlock visitConstant(@NotNull JetTemplateParser.ConstantContext ctx) {
        return null;
    }


    public CodeBlock visitPut_directive(@NotNull JetTemplateParser.Put_directiveContext ctx) {
        return null;
    }

    public CodeBlock visitExpr_array_list(@NotNull JetTemplateParser.Expr_array_listContext ctx) {
        ParseTree items = ctx.getChild(1);

        for (int i = 0; i < items.getChildCount(); i++) {
            CodeLet tmp=pushPeekCodeLet();
            items.getChild(i).accept(this);
            popCodeLet();
            peekCodeLet().code(tmp);
        }
        peekCodeLet().codeBefore("new Object[]{").code("}");
        return null;
    }

    public CodeBlock visitExpr_conditional_ternary(@NotNull JetTemplateParser.Expr_conditional_ternaryContext ctx) {
        CodeLet condition = new CodeLet();
        pushCodeLet(condition);
        ctx.expression(0).accept(this);
        popCodeLet();
        CodeLet left = new CodeLet();
        pushCodeLet(left);
        ctx.expression(1).accept(this);
        popCodeLet();
        CodeLet right = new CodeLet();
        pushCodeLet(right);
        ctx.expression(2).accept(this);
        popCodeLet();
        peekCodeLet().code("U.b(%s)?%s:%s", condition, left, right);
        return null;
    }

    public CodeBlock visitExpr_method_invocation(@NotNull JetTemplateParser.Expr_method_invocationContext ctx) {
        return null;
    }

    public CodeBlock visitFor_expression(@NotNull JetTemplateParser.For_expressionContext ctx) {
        String name = ctx.IDENTIFIER().getText();
        pushCodeLet();
        ctx.expression().accept(this);
        CodeLet code = new CodeLet().code("ForIterator $").code(name).code("For = new ForIterator(").code(peekCodeLet()).lineCode(");");
        popCodeLet();
        peekCodeBlock().subCode(code);
        peekCodeBlock().subCode(new CodeLet().code("$context.put(\"$").code(name).code("For\",$").code(name).lineCode("For);"));
        peekCodeBlock().subCode(new CodeLet().code("while($").code(name).lineCode("For.hasNext()){"));
        CodeBlock assign = new CodeBlock().tabIndent(1);
        assign.footer(new CodeLet().code("$context.put(\"").code(name).code("\",$").code(name).lineCode("For.next());")).tabIndent(1);
        peekCodeBlock().subCode(assign);
        return null;
    }

    public CodeBlock visitDefine_expression(@NotNull JetTemplateParser.Define_expressionContext ctx) {
        return null;
    }


    public CodeBlock visitStop_directive(@NotNull JetTemplateParser.Stop_directiveContext ctx) {
        JetTemplateParser.ExpressionContext expression = ctx.expression();
        if (expression != null) {
            pushCodeLet();
            expression.accept(this);
            CodeBlock ifCodeBlock = new CodeBlock().header(peekCodeLet().codeBefore("if(U.b(").lineCode(")){")).footer(new CodeLet().lineCode("}"));
            popCodeLet();
            ifCodeBlock.subCode(new CodeLet().lineCode("return;"));
            peekCodeBlock().subCode(ifCodeBlock);
        } else {
            peekCodeBlock().subCode(new CodeLet().lineCode("return;"));
        }
        return null;
    }

    public CodeBlock visitBreak_directive(@NotNull JetTemplateParser.Break_directiveContext ctx) {
        JetTemplateParser.ExpressionContext expression = ctx.expression();
        if (expression != null) {
            pushCodeLet();
            expression.accept(this);
            CodeBlock ifCodeBlock = new CodeBlock().header(peekCodeLet().codeBefore("if(U.b(").lineCode(")){")).footer(new CodeLet().lineCode("}"));
            popCodeLet();
            ifCodeBlock.subCode(new CodeLet().lineCode("break;"));
            peekCodeBlock().subCode(ifCodeBlock);
        } else {
            peekCodeBlock().subCode(new CodeLet().lineCode("break;"));
        }
        return null;
    }

    public CodeBlock visitFor_directive(@NotNull JetTemplateParser.For_directiveContext ctx) {
        String name = ctx.getChild(1).getChild(1).getText();
        ctx.for_expression().accept(this);
        CodeBlock forCodeBlock = new CodeBlock();
        peekCodeBlock().subCode(forCodeBlock);
        forCodeBlock.footer(new CodeLet().lineCode("}"));
        pushCodeBlock(forCodeBlock);
        ctx.block().accept(this);
        popCodeBlock();
        //添加清理处理
        peekCodeBlock().subCode(new CodeLet().code("$context.remove(\"$").code(name).lineCode("For\");"));
        peekCodeBlock().subCode(new CodeLet().code("$context.remove(\"").code(name).lineCode("\");"));

        return null;
    }

    void pushCodeBlock(CodeBlock codeBlock) {
        codeBlocks.push(codeBlock);
    }
    void pushCodeBlock() {
        pushCodeBlock(new CodeBlock());
    }

    void popCodeBlock() {
         codeBlocks.pop();
    }


    void popCodeLet() {
        codeLets.pop();
    }

    void pushCodeLet(CodeLet codeLet) {
        codeLets.push(codeLet);
    }
    void pushCodeLet() {
        pushCodeLet(new CodeLet());
    }
    CodeLet peekCodeLet() {
        return codeLets.peek();
    }
    CodeLet pushPeekCodeLet() {
        pushCodeLet();
        return codeLets.peek();
    }

    CodeBlock peekCodeBlock() {
        return codeBlocks.peek();
    }
    CodeBlock pushPeekCodeBlock() {
        pushCodeBlock();
        return codeBlocks.peek();
    }
    public CodeBlock visitElseif_directive(@NotNull JetTemplateParser.Elseif_directiveContext ctx) {
        pushCodeLet();
        ctx.expression().accept(this);
        CodeBlock elseifBlock = new CodeBlock().header(peekCodeLet().codeBefore("}else if(U.b(").lineCode(")){")).tabIndent(-1);
        popCodeLet();
        peekCodeBlock().subCode(elseifBlock);
        ctx.block().accept(this);
        return null;
    }

    public CodeBlock visitExpr_math_unary_prefix(@NotNull JetTemplateParser.Expr_math_unary_prefixContext ctx) {
        return null;
    }

    public CodeBlock visitExpr_group(@NotNull JetTemplateParser.Expr_groupContext ctx) {
        peekCodeLet().code("(");
        ctx.expression().accept(this);
        peekCodeLet().code(")");
        return null;
    }

    public CodeBlock visitExpr_constant(@NotNull JetTemplateParser.Expr_constantContext ctx) {
        peekCodeLet().code(ctx.getText());
        return null;
    }


}
