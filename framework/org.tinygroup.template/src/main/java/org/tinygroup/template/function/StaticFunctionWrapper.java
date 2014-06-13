package org.tinygroup.template.function;

import org.tinygroup.template.TemplateException;

/**
 * Created by luoguo on 2014/6/9.
 */
public class StaticFunctionWrapper extends AbstractFunctionWrapper{

    public StaticFunctionWrapper(String name) {
        super(name);
    }


    public Object execute(Object... parameters) throws TemplateException {
        return null;
    }
}