package org.tinygroup.template.impl;

import org.tinygroup.template.Macro;
import org.tinygroup.template.TemplateContext;
import org.tinygroup.template.TemplateException;

import java.io.IOException;
import java.io.Writer;

/**
 * 抽象宏
 * Created by luoguo on 2014/6/6.
 */
public abstract class AbstractMacro implements Macro {
    private String name;
    private String[] parameterNames;

    public void setName(String name) {
        this.name = name;
    }

    public void setParameterNames(String[] parameterNames) {
        this.parameterNames = parameterNames;
    }

    protected void init(String name, String[] parameterNames) {
        this.name = name;
        this.parameterNames = parameterNames;
    }
    public void render(TemplateContext $context, Writer writer) throws TemplateException {
        try {
            renderTemplate($context, writer);
            writer.flush();
        } catch (IOException e) {
            throw new TemplateException(e);
        }
    }

    protected abstract void renderTemplate(TemplateContext $context, Writer $writer)throws IOException,TemplateException;


    public String getName() {
        return name;
    }

    public String[] getParameterNames() {
        return parameterNames;
    }

}
