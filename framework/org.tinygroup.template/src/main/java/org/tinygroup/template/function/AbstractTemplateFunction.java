package org.tinygroup.template.function;

import org.tinygroup.template.TemplateEngine;
import org.tinygroup.template.TemplateException;
import org.tinygroup.template.TemplateFunction;

/**
 * Created by luoguo on 2014/6/9.
 */
public abstract class AbstractTemplateFunction implements TemplateFunction {
    private final String names;

    public AbstractTemplateFunction(String name) {
        this.names = name;
    }

    private TemplateEngine templateEngine;

    public TemplateEngine getTemplateEngine() {
        return templateEngine;
    }


    public String getNames() {
        return names;
    }


    public String getBindingTypes() {
        return null;
    }


    public void setTemplateEngine(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    protected TemplateException notSupported(Object[] parameters) throws TemplateException {
        StringBuffer sb = new StringBuffer(getNames());
        sb.append("不支持下面的参数：[\n");
        for (Object parameter : parameters) {
            sb.append(parameter.getClass().getName()).append("\n");
        }
        sb.append("]\n");
        throw new TemplateException(sb.toString());
    }
}

