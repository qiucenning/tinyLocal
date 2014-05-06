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
package org.tinygroup.exception;

import java.util.Locale;

import org.tinygroup.context.Context;

public class TinyBizRuntimeException extends BaseRuntimeException {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1694628104824119327L;

	public TinyBizRuntimeException(Throwable throwable) {
		super(throwable);
	}
	public TinyBizRuntimeException(Throwable throwable, String code) {
		super(throwable,code);
	}

	public TinyBizRuntimeException(Throwable throwable, String code,Object...args) {
		super(throwable,code,args);
	}
	public TinyBizRuntimeException(String code, Object... args) {
		super(code, args);
	}

	public TinyBizRuntimeException(String code) {
		super(code);
	}

	public TinyBizRuntimeException(String code, Locale locale) {
		super(code, locale);
	}

	public TinyBizRuntimeException(String code, Locale locale, Object... args) {
		super(code, locale, args);
	}

	public TinyBizRuntimeException(String code, Context context, Locale locale) {
		super(code, context, locale);
	}

	public TinyBizRuntimeException(String code, Context context) {
		super(code, context);
	}

}
