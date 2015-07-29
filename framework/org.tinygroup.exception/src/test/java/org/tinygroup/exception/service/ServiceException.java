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
package org.tinygroup.exception.service;

import org.tinygroup.context.Context;
import org.tinygroup.exception.TinyBizRuntimeException;

import java.util.Locale;

/**
 * 
 * @author renhui
 *
 */
public class ServiceException extends TinyBizRuntimeException {

	public ServiceException(String errorCode, Context context, Locale locale) {
		super(errorCode, context, locale);
	}

	public ServiceException(String errorCode, Context context) {
		super(errorCode, context);
	}

	public ServiceException(String errorCode, Object... params) {
		super(errorCode, params);
	}

	public ServiceException(String errorCode, String defaultErrorMsg,
			Context context, Locale locale) {
		super(errorCode, defaultErrorMsg, context, locale);
	}

	public ServiceException(String errorCode, String defaultErrorMsg,
			Locale locale, Object... params) {
		super(errorCode, defaultErrorMsg, locale, params);
	}

	public ServiceException(String errorCode, String defaultErrorMsg,
			Locale locale, Throwable throwable, Object... params) {
		super(errorCode, defaultErrorMsg, locale, throwable, params);
	}

	public ServiceException(String errorCode, String defaultErrorMsg,
			Object... params) {
		super(errorCode, defaultErrorMsg, params);
	}

	public ServiceException(String errorCode, String defaultErrorMsg,
			Throwable throwable, Object... params) {
		super(errorCode, defaultErrorMsg, throwable, params);
	}

	
}
