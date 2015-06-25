/**
 *  Copyright (c) 1997-2013, www.tinygroup.org (tinygroup@126.com).
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
package org.tinygroup.jspengine.el.parser;

import org.tinygroup.jspengine.el.lang.EvaluationContext;

import javax.el.ELException;
import java.util.Collection;
import java.util.Map;


/**
 * @author Jacob Hookom [jacob@hookom.net]
 * @version $Change: 181177 $$DateTime: 2001/06/26 08:45:09 $$Author: tcfujii $
 */
public final class AstEmpty extends SimpleNode {
    public AstEmpty(int id) {
        super(id);
    }

    public Class getType(EvaluationContext ctx)
            throws ELException {
        return Boolean.class;
    }

    public Object getValue(EvaluationContext ctx)
            throws ELException {
        Object obj = this.children[0].getValue(ctx);
        if (obj == null) {
            return Boolean.TRUE;
        } else if (obj instanceof String) {
            return Boolean.valueOf(((String) obj).length() == 0);
        } else if (obj instanceof Object[]) {
            return Boolean.valueOf(((Object[]) obj).length == 0);
        } else if (obj instanceof Collection) {
            return Boolean.valueOf(((Collection) obj).isEmpty());
        } else if (obj instanceof Map) {
            return Boolean.valueOf(((Map) obj).isEmpty());
        }
        return Boolean.FALSE;
    }
}
