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
package org.tinygroup.velocity.directive;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;
import org.tinygroup.commons.i18n.LocaleUtil;
import org.tinygroup.i18n.I18nMessage;
import org.tinygroup.i18n.I18nMessageFactory;
import org.tinygroup.velocity.TinyVelocityContext;

import java.io.IOException;
import java.io.Writer;
import java.util.Locale;

public class I18nDirective extends Directive {

	private static final String I18N = "i";
	private static I18nMessage i18nMessage = null;// 需要在启动的时候注入进来
	static {
		i18nMessage = I18nMessageFactory.getI18nMessages();
	}

	
	public String getName() {
		return I18N;
	}

	
	public int getType() {
		return LINE;
	}

	
	public boolean render(InternalContextAdapter context, Writer writer,
			Node node) throws IOException {
		String code = node.jjtGetChild(0).value(context).toString();
		VelocityContext velocityContext = (VelocityContext) context
				.getInternalUserContext();
		TinyVelocityContext tinyVelocityContext = (TinyVelocityContext) velocityContext
				.getChainedContext();
		Locale locale = LocaleUtil.getContext().getLocale();
		String value = i18nMessage.getMessage(code, locale,
				tinyVelocityContext.getContext());
		if (value != null) {
			writer.write(value);
			return true;
		}
		return false;
	}

}
