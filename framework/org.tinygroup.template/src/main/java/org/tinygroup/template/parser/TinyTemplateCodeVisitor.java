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

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.tinygroup.template.parser.grammer.TinyTemplateParser;
import org.tinygroup.template.parser.grammer.TinyTemplateParserVisitor;

import java.util.List;
import java.util.Stack;

// Visitor 模式访问器，用来生成 Java 代码
public class TinyTemplateCodeVisitor extends AbstractParseTreeVisitor<CodeBlock> implements TinyTemplateParserVisitor<CodeBlock> {
    private TinyTemplateParser parser = null;
    private Stack<CodeBlock> codeBlocks = new Stack<CodeBlock>();
    private Stack<CodeLet> codeLets = new Stack<CodeLet>();
    private CodeBlock initCodeBlock = null;
    private CodeBlock macroCodeBlock = null;


    public TinyTemplateCodeVisitor(TinyTemplateParser parser) {
        this.parser = parser;
    }

    public CodeBlock visitExpression_list(@NotNull TinyTemplateParser.Expression_listContext ctx) {

        List<TinyTemplateParser.ExpressionContext> expression_list = ctx.expression();
        int i = 0;
        for (TinyTemplateParser.ExpressionContext expression : expression_list) {
            CodeLet exp = pushPeekCodeLet();
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

    public CodeBlock visitInvalid_directive(@NotNull TinyTemplateParser.Invalid_directiveContext ctx) {
        throw reportError("Missing arguments for " + ctx.getText() + " directive.", ctx);
    }


    public CodeBlock visitCall_directive(@NotNull TinyTemplateParser.Call_directiveContext ctx) {
        CodeBlock callMacro = new CodeBlock();
        CodeLet nameCodeBlock = pushPeekCodeLet();
        ctx.expression().accept(this);
        popCodeLet();

        String name = nameCodeBlock.toString();

        callMacro.subCode(String.format("$macro=getTemplateEngine().findMacro(%s,$template,$context);", name));
        callMacro.subCode("$newContext = new TemplateContextDefault();");
        callMacro.subCode("$newContext.setParent(\"$context\");");
        if (ctx.para_expression_list() != null) {
            List<TinyTemplateParser.Para_expressionContext> expList = ctx.para_expression_list().para_expression();
            if (expList != null) {
                pushCodeBlock(callMacro);
                int i = 0;
                for (TinyTemplateParser.Para_expressionContext visitPara_expression : expList) {
                    CodeLet expression = new CodeLet();
                    pushCodeLet(expression);
                    if (visitPara_expression.getChildCount() == 3) {
                        //如果是带参数的
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
        callMacro.subCode(String.format("$macro.render($template,$newContext,$writer);"));
        return callMacro;
    }

    public CodeBlock visitElse_directive(@NotNull TinyTemplateParser.Else_directiveContext ctx) {
        CodeBlock elseBlock = new CodeBlock().subCode(new CodeLet().lineCode("}else{")).tabIndent(-1);
        peekCodeBlock().subCode(elseBlock);
        ctx.block().accept(this);
        return null;
    }

    public CodeBlock visitType(@NotNull TinyTemplateParser.TypeContext ctx) {
        return null;
    }

    public CodeBlock visitExpr_hash_map(@NotNull TinyTemplateParser.Expr_hash_mapContext ctx) {
        TinyTemplateParser.Hash_map_entry_listContext hash_map_entry_list = ctx.hash_map_entry_list();
        if (hash_map_entry_list != null) {
            peekCodeLet().code("new TemplateMap()").code(hash_map_entry_list.accept(this).toString()).code("");
        }
        return null;
    }

    public CodeBlock visitContinue_directive(@NotNull TinyTemplateParser.Continue_directiveContext ctx) {
        TinyTemplateParser.ExpressionContext expression = ctx.expression();
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


    public CodeBlock visitExpr_field_access(@NotNull TinyTemplateParser.Expr_field_accessContext ctx) {
        CodeLet exp = pushPeekCodeLet();
        ctx.expression().accept(this);
        popCodeLet();
        peekCodeLet().code(ctx.getChild(1).getText().equals("?.") ? "U.sp(" : "U.p(").code(exp).code(",\"").code(ctx.IDENTIFIER().getText()).code("\")");
        return null;
    }

    public CodeBlock visitExpr_compare_condition(@NotNull TinyTemplateParser.Expr_compare_conditionContext ctx) {
        CodeLet left = pushPeekCodeLet();
        ctx.expression(0).accept(this);
        popCodeLet();
        left.codeBefore("U.b(").code(")");
        CodeLet right = pushPeekCodeLet();
        ctx.expression(1).accept(this);
        popCodeLet();
        right.codeBefore("U.b(").code(")");
        String op = ctx.getChild(1).getText();
        peekCodeLet().code(left).code(op).code(right);
        return null;
    }


    public CodeBlock visitExpr_function_call(@NotNull TinyTemplateParser.Expr_function_callContext ctx) {
        String functionName = ctx.getChild(0).getText();

        peekCodeLet().codeBefore("getTemplateEngine().executeFunction(\"").code(functionName).code("\"");
        TinyTemplateParser.Expression_listContext list = ctx.expression_list();
        if (list != null) {
            peekCodeLet().code(",");
            list.accept(this);
        }
        peekCodeLet().code(")");
        return null;
    }

    public CodeBlock visitType_array_suffix(@NotNull TinyTemplateParser.Type_array_suffixContext ctx) {
        return null;
    }

    public CodeBlock visitMacro_directive(@NotNull TinyTemplateParser.Macro_directiveContext ctx) {
        String name = ctx.getChild(0).getText();
        name = name.substring(6, name.length() - 1).trim();
        initCodeBlock.subCode(new CodeLet().lineCode("getMacroMap().put(\"%s\", new %s());", name, name));
        //TODO
        CodeBlock macro = new CodeBlock();
        TinyTemplateParser.Define_expression_listContext define_expression_list = ctx.define_expression_list();
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

    public CodeBlock visitExpr_compare_equality(@NotNull TinyTemplateParser.Expr_compare_equalityContext ctx) {
        peekCodeLet().code("O.e(\"").code(ctx.getChild(1).getText()).code("\",");
        ctx.expression(0).accept(this);
        peekCodeLet().code(",");
        ctx.expression(1).accept(this);
        peekCodeLet().code(")");
        return null;
    }

    public CodeBlock visitValue(@NotNull TinyTemplateParser.ValueContext ctx) {
        CodeBlock valueCodeBlock = new CodeBlock();
        pushCodeLet();
        if (ctx.getChild(0).getText().equals("$${")) {
            peekCodeLet().code("write($writer,U.getI18n($template.getTemplateEngine().getI18nVistor(),$context,\"").code(ctx.identify_list().getText()).lineCode("\"));");
        } else {
            ctx.expression().accept(this);
            Token token = ((TerminalNode) ctx.getChild(0)).getSymbol();
            if (token.getType() == TinyTemplateParser.VALUE_ESCAPED_OPEN) {
                peekCodeLet().codeBefore("U.escapeHtml((").code("))");
            }
//        }
            peekCodeLet().codeBefore("write($writer,").lineCode(");");
        }
        valueCodeBlock.subCode(peekCodeLet());
        popCodeLet();
        return valueCodeBlock;
    }

    public CodeBlock visitExpr_math_binary_bitwise(@NotNull TinyTemplateParser.Expr_math_binary_bitwiseContext ctx) {
        peekCodeLet().code("O.e(\"").code(ctx.getChild(1).getText()).code("\",");
        ctx.expression(0).accept(this);
        peekCodeLet().code(",");
        ctx.expression(1).accept(this);
        peekCodeLet().code(")");
        return null;
    }


    public CodeBlock visitHash_map_entry_list(@NotNull TinyTemplateParser.Hash_map_entry_listContext ctx) {
        List<TinyTemplateParser.ExpressionContext> expression_list = ctx.expression();
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
            keyPair.code(".putItem(").code(keyCodeLet).code(",").code(valueCodeLet).code(")");
        }
        return result;
    }

    public CodeBlock visitDirective(@NotNull TinyTemplateParser.DirectiveContext ctx) {
        return ctx.getChild(0).accept(this);
    }

    public CodeBlock visitTemplate(@NotNull TinyTemplateParser.TemplateContext ctx) {
        CodeBlock templateCodeBlock = getTemplateCodeBlock();
        CodeBlock classCodeBlock = getClassCodeBlock();
        templateCodeBlock.subCode(classCodeBlock);
        CodeBlock renderMethodCodeBlock = getTemplateRenderCodeBlock();
        classCodeBlock.subCode(renderMethodCodeBlock);
        CodeBlock getTemplatePathMethod = getTemplatePathMethodCodeBlock();
        classCodeBlock.subCode(getTemplatePathMethod);
        pushCodeBlock(renderMethodCodeBlock);
        renderMethodCodeBlock.subCode(ctx.block().accept(this));
        popCodeBlock();
        return templateCodeBlock;
    }

    private CodeBlock getTemplateRenderCodeBlock() {
        CodeBlock renderMethod = new CodeBlock();
        renderMethod.header(new CodeLet().lineCode("protected void renderTemplate(TemplateContext $context, Writer $writer) throws IOException, TemplateException{")).footer(new CodeLet().lineCode("}"));
        renderMethod.subCode("Macro $macro=null;");
        renderMethod.subCode("Template $template=this;");
        renderMethod.subCode("TemplateContext $newContext=null;");

        return renderMethod;
    }

    private CodeBlock getMacroRenderCodeBlock() {
        CodeBlock renderMethod = new CodeBlock();
        renderMethod.header(new CodeLet().lineCode("protected void renderMacro(Template $template, TemplateContext $context, Writer $writer) throws IOException, TemplateException{")).footer(new CodeLet().lineCode("}"));
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

    public CodeBlock visitExpr_compare_not(@NotNull TinyTemplateParser.Expr_compare_notContext ctx) {
        return null;
    }


    public CodeBlock visitIdentify_list(@NotNull TinyTemplateParser.Identify_listContext ctx) {

        return null;
    }

    public CodeBlock visitText(@NotNull TinyTemplateParser.TextContext ctx) {
        Token token = ((TerminalNode) ctx.getChild(0)).getSymbol();
        String text = token.getText();
        if (text.equals("\r\n") || text.equals("\n") || text.equals("\r")) {
            return null;
        }
        switch (token.getType()) {
            case TinyTemplateParser.COMMENT_LINE:
            case TinyTemplateParser.COMMENT_BLOCK1:
            case TinyTemplateParser.COMMENT_BLOCK2:
                return null;
            case TinyTemplateParser.TEXT_PLAIN:
                break;
            case TinyTemplateParser.TEXT_CDATA:
                text = text.substring(3, text.length() - 3).trim();
                break;
            case TinyTemplateParser.TEXT_ESCAPED_CHAR:
                text = text.substring(1);
                break;
        }
        return new CodeBlock().header(new CodeLet().code("write($writer,\"").code(escapeJavaStyleString(text).toString()).lineCode("\");"));
    }

    private static StringBuffer escapeJavaStyleString(String str) {
        StringBuffer stringBuffer = new StringBuffer();
        int sz;
        sz = str.length();
        for (int i = 0; i < sz; i++) {
            char ch = str.charAt(i);

            // handle unicode
            if (ch < 32) {
                switch (ch) {
                    case '\b':
                        stringBuffer.append('\\');
                        stringBuffer.append('b');
                        break;
                    case '\n':
                        stringBuffer.append('\\');
                        stringBuffer.append('n');
                        break;
                    case '\t':
                        stringBuffer.append('\\');
                        stringBuffer.append('t');
                        break;
                    case '\f':
                        stringBuffer.append('\\');
                        stringBuffer.append('f');
                        break;
                    case '\r':
                        stringBuffer.append('\\');
                        stringBuffer.append('r');
                        break;
                    default:
                        stringBuffer.append(ch);
                        break;
                }
            } else {
                switch (ch) {
                    case '"':
                        stringBuffer.append('\\');
                        stringBuffer.append('"');
                        break;
                    case '\\':
                        stringBuffer.append('\\');
                        stringBuffer.append('\\');
                        break;
                    default:
                        stringBuffer.append(ch);
                        break;
                }
            }
        }
        return stringBuffer;
    }

    public CodeBlock visitExpr_identifier(@NotNull TinyTemplateParser.Expr_identifierContext ctx) {
        String name = ctx.IDENTIFIER().getText();
        if (name.startsWith("$")) {
            name = name.substring(1);
        }
        peekCodeLet().code("U.v($context,\"" + name + "\")");
        return null;
    }


    public CodeBlock visitIf_directive(@NotNull TinyTemplateParser.If_directiveContext ctx) {
        CodeBlock ifCodeBlock = pushPeekCodeBlock();
        pushCodeLet();
        ctx.expression().accept(this);
        ifCodeBlock.header(peekCodeLet().codeBefore("if(U.b(").lineCode(")){"));
        popCodeLet();
        ifCodeBlock.footer(new CodeLet().lineCode("}"));
        ctx.block().accept(this);
        List<TinyTemplateParser.Elseif_directiveContext> elseif_directive_list = ctx.elseif_directive();
        for (TinyTemplateParser.Elseif_directiveContext elseif_directive : elseif_directive_list) {
            elseif_directive.accept(this);
        }
        TinyTemplateParser.Else_directiveContext else_directive = ctx.else_directive();
        if (else_directive != null) {
            else_directive.accept(this);
        }
        popCodeBlock();
        return ifCodeBlock;
    }

    public CodeBlock visitExpr_math_unary_suffix(@NotNull TinyTemplateParser.Expr_math_unary_suffixContext ctx) {
        peekCodeLet().code("O.e(\"l").code(ctx.getChild(1).getText()).code("\",");
        ctx.expression().accept(this);
        peekCodeLet().code(")");
        return null;
    }

    @Override
    public CodeBlock visitBodycontent_directive(@NotNull TinyTemplateParser.Bodycontent_directiveContext ctx) {
        CodeBlock codeBlock = new CodeBlock();
        codeBlock.subCode("$macro= (Macro) $context.getItemMap().get(\"bodyContent\");");
        codeBlock.subCode("if($macro!=null) {");
        codeBlock.subCode("    $macro.render($template,$context,$writer);");
        codeBlock.subCode("}");
        return codeBlock;
    }

    public CodeBlock visitDefine_expression_list(@NotNull TinyTemplateParser.Define_expression_listContext ctx) {
        if (peekCodeLet().length() > 0) {
            peekCodeLet().code(",");
        }
        peekCodeLet().code("\"%s\"", ctx.getChild(0).getText());
        return null;
    }


    public CodeBlock visitCall_macro_directive(@NotNull TinyTemplateParser.Call_macro_directiveContext ctx) {
        CodeBlock callMacro = new CodeBlock();
        String name = ctx.getChild(0).getText();
        name = name.substring(1, name.length());
        if (name.endsWith("(")) {
            name = name.substring(0, name.length() - 1);
        }
        callMacro.subCode(String.format("$macro=getTemplateEngine().findMacro(\"%s\",$template,$context);", name));
        callMacro.subCode("$newContext = new TemplateContextDefault();");
        callMacro.subCode("$newContext.setParent(\"$context\");");
        if (ctx.para_expression_list() != null) {
            TinyTemplateParser.Para_expression_listContext expList = ctx.para_expression_list();
            if (expList != null) {
                pushCodeBlock(callMacro);
                int i = 0;
                for (TinyTemplateParser.Para_expressionContext visitPara_expression : expList.para_expression()) {
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
        callMacro.subCode(String.format("$macro.render($template,$newContext,$writer);"));
        return callMacro;
    }

    public CodeBlock visitType_name(@NotNull TinyTemplateParser.Type_nameContext ctx) {
        return null;
    }

    public CodeBlock visitType_list(@NotNull TinyTemplateParser.Type_listContext ctx) {
        return null;
    }

    public CodeBlock visitCall_macro_block_directive(@NotNull TinyTemplateParser.Call_macro_block_directiveContext ctx) {
        CodeBlock callMacro = new CodeBlock();
        String name = ctx.getChild(0).getText();
        name = name.substring(2, name.length() - 1).trim();
        callMacro.subCode(String.format("$macro=getTemplateEngine().findMacro(\"%s\",$template,$context);", name));
        callMacro.subCode("$newContext=new TemplateContextDefault();");
        callMacro.subCode("$newContext.setParent(\"$context\");");
        TinyTemplateParser.Para_expression_listContext expList = ctx.para_expression_list();
        if (expList != null) {
            pushCodeBlock(callMacro);
            int i = 0;
            for (TinyTemplateParser.Para_expressionContext visitPara_expression : expList.para_expression()) {
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
        callMacro.subCode(String.format("$macro.render($template,$newContext,$writer);", name));

        bodyContentMacro.header("$newContext.put(\"bodyContent\",new AbstractMacro() {");
        CodeBlock render = getMacroRenderCodeBlock();
        bodyContentMacro.subCode(render);

        pushCodeBlock(render);
        ctx.block().accept(this);
        popCodeBlock();
        bodyContentMacro.footer("});");

        //Body部分创建新的类，然后传入要调用的宏
        return callMacro;
    }

    public CodeBlock visitInclude_directive(@NotNull TinyTemplateParser.Include_directiveContext ctx) {
        CodeBlock include = new CodeBlock();
        CodeLet path = pushPeekCodeLet();
        ctx.expression().accept(this);
        popCodeLet();
        CodeLet map = pushPeekCodeLet();
        if (ctx.hash_map_entry_list() != null) {
            peekCodeLet().code("new TemplateMap()").code(ctx.hash_map_entry_list().accept(this).toString()).code("");
        }
        popCodeLet();
        //getTemplateEngine().renderTemplate("aa",$context,$writer);
        include.subCode("$newContext = new TemplateContextDefault();");
        if (map.length() > 0) {
            include.subCode(String.format("$newContext.putAll(%s);", map));
        }
        include.subCode("$context.putSubContext(\"$newContext\",$newContext);");
        include.subCode(String.format("getTemplateEngine().renderTemplate(U.getPath(getPath(),%s),$newContext,$writer);", path));
        include.subCode("$context.removeSubContext(\"$newContext\");");
        return include;
    }

    public CodeBlock visitPara_expression(@NotNull TinyTemplateParser.Para_expressionContext ctx) {

        return null;
    }


    public CodeBlock visitCall_block_directive(@NotNull TinyTemplateParser.Call_block_directiveContext ctx) {
        CodeBlock callMacro = new CodeBlock();
        CodeLet nameCodeBlock = pushPeekCodeLet();
        ctx.expression().accept(this);
        popCodeLet();

        String name = nameCodeBlock.toString();
        name = name.substring(2, name.length() - 1).trim();
        callMacro.subCode(String.format("$macro=getTemplateEngine().findMacro(\"%s\",$template,$context);", name));
        callMacro.subCode("$newContext=new TemplateContextDefault();");
        callMacro.subCode("$context.putSubContext(\"$newContext\",$newContext);");
        List<TinyTemplateParser.Para_expressionContext> expList = ctx.para_expression_list().para_expression();
        if (expList != null) {
            pushCodeBlock(callMacro);
            int i = 0;
            for (TinyTemplateParser.Para_expressionContext visitPara_expression : expList) {
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
        callMacro.subCode(String.format("$macro.render($template,$newContext,$writer);", name));

        bodyContentMacro.header("$newContext.put(\"bodyContent\",new AbstractMacro() {");
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

    public CodeBlock visitExpr_array_get(@NotNull TinyTemplateParser.Expr_array_getContext ctx) {
        ctx.expression(0).accept(this);
        peekCodeLet().codeBefore("U.a(").code(",");
        ctx.expression(1).accept(this);
        peekCodeLet().code(")");
        return null;
    }

    public CodeBlock visitBlock(@NotNull TinyTemplateParser.BlockContext ctx) {
        for (int i = 0; i < ctx.getChildCount(); i++) {
            ParseTree node = ctx.children.get(i);
            CodeBlock codeBlock = node.accept(this);
            if (codeBlock != null) {
                peekCodeBlock().subCode(codeBlock);
            }
        }
        return null;
    }

    public CodeBlock visitExpr_compare_relational(@NotNull TinyTemplateParser.Expr_compare_relationalContext ctx) {
        peekCodeLet().code("O.e(\"").code(ctx.getChild(1).getText()).code("\",");
        ctx.expression(0).accept(this);
        peekCodeLet().code(",");
        ctx.expression(1).accept(this);
        peekCodeLet().code(")");
        return null;
    }

    public CodeBlock visitType_arguments(@NotNull TinyTemplateParser.Type_argumentsContext ctx) {
        return null;
    }


    public CodeBlock visitExpr_math_binary_basic(@NotNull TinyTemplateParser.Expr_math_binary_basicContext ctx) {
        peekCodeLet().code("O.e(\"").code(ctx.getChild(1).getText()).code("\",");
        ctx.expression(0).accept(this);
        peekCodeLet().code(",");
        ctx.expression(1).accept(this);
        peekCodeLet().code(")");
        return null;
    }

    public CodeBlock visitPara_expression_list(@NotNull TinyTemplateParser.Para_expression_listContext ctx) {

        return null;
    }

    public CodeBlock visitSet_expression(@NotNull TinyTemplateParser.Set_expressionContext ctx) {
        CodeBlock codeBlock = new CodeBlock();
        CodeLet codeLet = pushPeekCodeLet();
        codeBlock.header(codeLet);
        ctx.expression().accept(this);
        popCodeLet();
        codeLet.codeBefore("$context.put(\"" + ctx.getChild(0).getText() + "\",").lineCode(");");
        return codeBlock;
    }

    public CodeBlock visitTerminal(@org.antlr.v4.runtime.misc.NotNull org.antlr.v4.runtime.tree.TerminalNode node) {
        peekCodeLet().code(node.getText());
        return null;
    }


    public CodeBlock visitSet_directive(@NotNull TinyTemplateParser.Set_directiveContext ctx) {
        List<TinyTemplateParser.Set_expressionContext> set_expression_list = ctx.set_expression();
        for (TinyTemplateParser.Set_expressionContext node : set_expression_list) {
            peekCodeBlock().subCode(node.accept(this));
        }
        return null;
    }


    public CodeBlock visitConstant(@NotNull TinyTemplateParser.ConstantContext ctx) {
        return null;
    }


    public CodeBlock visitExpr_member_function_call(@NotNull TinyTemplateParser.Expr_member_function_callContext ctx) {
        ctx.expression().accept(this);
        String functionName = ctx.IDENTIFIER().getText();
        peekCodeLet().codeBefore(ctx.getChild(1).getText().equals(".") ? "U.c($template," : "U.sc($template,").code(",\"").code(functionName).code("\"");
        TinyTemplateParser.Expression_listContext list = ctx.expression_list();
        if (list != null) {
            peekCodeLet().code(",");
            list.accept(this);
        }
        peekCodeLet().code(")");
        return null;
    }


    public CodeBlock visitExpr_array_list(@NotNull TinyTemplateParser.Expr_array_listContext ctx) {
        ParseTree items = ctx.getChild(1);

        for (int i = 0; i < items.getChildCount(); i++) {
            CodeLet tmp = pushPeekCodeLet();
            items.getChild(i).accept(this);
            popCodeLet();
            peekCodeLet().code(tmp);
        }
        peekCodeLet().codeBefore("new Object[]{").code("}");
        return null;
    }


    public CodeBlock visitExpr_conditional_ternary(@NotNull TinyTemplateParser.Expr_conditional_ternaryContext ctx) {
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


    private RuntimeException reportError(String message, Object node) {
        if (node instanceof ParserRuleContext) {
            parser.notifyErrorListeners(((ParserRuleContext) node).getStart(), message, null);
        } else if (node instanceof TerminalNode) {
            parser.notifyErrorListeners(((TerminalNode) node).getSymbol(), message, null);
        } else if (node instanceof Token) {
            parser.notifyErrorListeners((Token) node, message, null);
        }
        return new SyntaxErrorException(message);
    }

    public CodeBlock visitFor_expression(@NotNull TinyTemplateParser.For_expressionContext ctx) {
        String name = ctx.IDENTIFIER().getText();
        pushCodeLet();
        ctx.expression().accept(this);
        peekCodeBlock().subCode(new CodeLet().code("$context.put(\"").code(name).code("For\",new ForIterator(").code(peekCodeLet()).lineCode("));"));
        peekCodeBlock().subCode(new CodeLet().code("while(((ForIterator)$context.get(\"").code(name).lineCode("For\")).hasNext()){"));
        CodeBlock assign = new CodeBlock().tabIndent(1);
        assign.footer(new CodeLet().code("$context.put(\"").code(name).code("\",((ForIterator)$context.get(\"").code(name).lineCode("For\")).next());")).tabIndent(1);
        peekCodeBlock().subCode(assign);
        popCodeLet();
        return null;
    }

    public CodeBlock visitDefine_expression(@NotNull TinyTemplateParser.Define_expressionContext ctx) {
        return null;
    }


    public CodeBlock visitExpr_math_binary_shift(@NotNull TinyTemplateParser.Expr_math_binary_shiftContext ctx) {
        peekCodeLet().code("O.e(\"").code(ctx.getChild(1).getText()).code("\",");
        ctx.expression(0).accept(this);
        peekCodeLet().code(",");
        ctx.expression(1).accept(this);
        peekCodeLet().code(")");
        return null;
    }


    public CodeBlock visitStop_directive(@NotNull TinyTemplateParser.Stop_directiveContext ctx) {
        TinyTemplateParser.ExpressionContext expression = ctx.expression();
        if (expression != null) {
            pushCodeLet();
            expression.accept(this);
            CodeBlock ifCodeBlock = new CodeBlock().header(peekCodeLet().codeBefore("if(U.b(").lineCode(")){")).footer(new CodeLet().lineCode("}"));
            popCodeLet();
            ifCodeBlock.subCode(new CodeLet().lineCode("return;"));
            peekCodeBlock().subCode(ifCodeBlock);
        } else {
            peekCodeBlock().subCode(new CodeLet().lineCode("if(true)return;"));
        }
        return null;
    }

    public CodeBlock visitBreak_directive(@NotNull TinyTemplateParser.Break_directiveContext ctx) {
        TinyTemplateParser.ExpressionContext expression = ctx.expression();
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

    public CodeBlock visitFor_directive(@NotNull TinyTemplateParser.For_directiveContext ctx) {
        String name = ctx.getChild(1).getChild(0).getText();
        ctx.for_expression().accept(this);
        CodeBlock forCodeBlock = new CodeBlock();
        peekCodeBlock().subCode(forCodeBlock);
        forCodeBlock.footer(new CodeLet().lineCode("}"));
        pushCodeBlock(forCodeBlock);
        ctx.block().accept(this);
        popCodeBlock();
        //添加清理处理

        TinyTemplateParser.Else_directiveContext else_directive = ctx.else_directive();
        if (else_directive != null) {
            CodeBlock elseCodeBlock = pushPeekCodeBlock().header("if(U.b(((ForIterator)$context.get(\"" + name + "For\")).getSize()>0)){").footer("}");
            else_directive.block().accept(this);
            popCodeBlock();
            peekCodeBlock().subCode(elseCodeBlock);
        }
        peekCodeBlock().subCode(new CodeLet().code("$context.remove(\"").code(name).lineCode("For\");"));
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

    public CodeBlock visitElseif_directive(@NotNull TinyTemplateParser.Elseif_directiveContext ctx) {
        pushCodeLet();
        ctx.expression().accept(this);
        CodeBlock elseifBlock = new CodeBlock().header(peekCodeLet().codeBefore("}else if(U.b(").lineCode(")){")).tabIndent(-1);
        popCodeLet();
        peekCodeBlock().subCode(elseifBlock);
        ctx.block().accept(this);
        return null;
    }

    public CodeBlock visitExpr_math_unary_prefix(@NotNull TinyTemplateParser.Expr_math_unary_prefixContext ctx) {
        peekCodeLet().code("O.e(\"l").code(ctx.getChild(0).getText()).code("\",");
        ctx.expression().accept(this);
        peekCodeLet().code(")");
        return null;
    }

    public CodeBlock visitExpr_group(@NotNull TinyTemplateParser.Expr_groupContext ctx) {
        peekCodeLet().code("(");
        ctx.expression().accept(this);
        peekCodeLet().code(")");
        return null;
    }

    public CodeBlock visitExpr_constant(@NotNull TinyTemplateParser.Expr_constantContext ctx) {
        String text = ctx.getText();
        if (text.startsWith("\'")) {
            text = text.substring(1, text.length() - 1);
            text = text.replaceAll("\\\\'", "'");
            text = text.replaceAll("\\\\\"", "\"");
            text = text.replaceAll("\"", "\\\\\"");
            peekCodeLet().code("\"").code(text).code("\"");
        } else {
            peekCodeLet().code(text);
        }
        return null;
    }


    public CodeBlock visitExpr_simple_condition_ternary(@NotNull TinyTemplateParser.Expr_simple_condition_ternaryContext ctx) {
        peekCodeLet().code("O.e(\"").code(ctx.getChild(1).getText()).code("\",");
        ctx.expression(0).accept(this);
        peekCodeLet().code(",");
        ctx.expression(1).accept(this);
        peekCodeLet().code(")");
        return null;
    }
}