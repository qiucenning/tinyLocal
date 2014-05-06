/**
 *  Copyright (c) 1997-2013, tinygroup.org (luo_guo@live.cn).
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
 * --------------------------------------------------------------------------
 *  版权 (c) 1997-2013, tinygroup.org (luo_guo@live.cn).
 *
 *  本开源软件遵循 GPL 3.0 协议;
 *  如果您不遵循此协议，则不被允许使用此文件。
 *  你可以从下面的地址获取完整的协议文本
 *
 *       http://www.gnu.org/licenses/gpl.html
 */
package org.tinygroup.weblayer.webcontext.parser.propertyedit;

import java.beans.PropertyEditorSupport;

import org.tinygroup.commons.tools.HumanReadableSize;

/**
 * 用于注入<code>HumanReadableSizeEditor</code>类型的property。
 *
 * @author renhui
 */
public class HumanReadableSizeEditor extends PropertyEditorSupport {
    
    public String getAsText() {
        Object value = getValue();

        if (value instanceof HumanReadableSize) {
            return ((HumanReadableSize) value).getHumanReadable();
        }

        return super.getAsText();
    }

    
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(new HumanReadableSize(text));
    }
}
