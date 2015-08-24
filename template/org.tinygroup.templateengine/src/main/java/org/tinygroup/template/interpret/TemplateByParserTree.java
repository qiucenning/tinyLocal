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
package org.tinygroup.template.interpret;

import org.tinygroup.template.TemplateContext;
import org.tinygroup.template.TemplateException;
import org.tinygroup.template.impl.AbstractTemplate;
import org.tinygroup.template.parser.grammer.TinyTemplateParser;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by luog on 15/7/18.
 */
public class TemplateByParserTree extends AbstractTemplate {
    private final TinyTemplateParser.TemplateContext tree;

    public TemplateByParserTree(TinyTemplateParser.TemplateContext tree) {
        this.tree = tree;
    }

    @Override
    protected void renderContent(TemplateContext context, OutputStream outputStream) throws IOException, TemplateException {

    }

    public String getPath() {
        return null;
    }
}
