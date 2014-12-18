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
package org.tinygroup.template.function;

import org.tinygroup.template.Macro;
import org.tinygroup.template.Template;
import org.tinygroup.template.TemplateContext;
import org.tinygroup.template.TemplateException;
import org.tinygroup.template.impl.TemplateContextDefault;

import java.io.StringWriter;

/**
 * Created by luoguo on 2014/6/9.
 */
public class CallMacroFunction extends AbstractTemplateFunction {

    public CallMacroFunction() {
        super("call,callMacro");
    }

    public Object execute(Template template, TemplateContext context, Object... parameters) throws TemplateException {
        if (parameters.length == 0 || !(parameters[0] instanceof String)) {
            notSupported(parameters);
        }
        String macroName = parameters[0].toString();
        Macro macro = getTemplateEngine().findMacro(macroName, template, context);
        TemplateContext newTemplateContext = null;
        if (parameters.length >= 2) {
            newTemplateContext = new TemplateContextDefault();
            for (int i = 0; i < macro.getParameterNames().size(); i++) {
                if (i + 1 >= parameters.length) {
                    break;
                }
                newTemplateContext.put(macro.getParameterNames().get(i), parameters[i + 1]);
            }
        }
        if (newTemplateContext == null) {
            newTemplateContext = context;
        }
        StringWriter stringWriter = new StringWriter();
        macro.render(template, context, newTemplateContext, stringWriter);
        return stringWriter.toString();
    }
}

