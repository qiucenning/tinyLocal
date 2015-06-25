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
package org.tinygroup.validate.validator;

import org.tinygroup.validate.ValidateResult;
import org.tinygroup.validate.impl.AbstractValidator;

public class NotNullValidator extends AbstractValidator {

	private static final String NOT_NULL_VALIDATOR_MESSAGE_KEY = "not_null_validator_message_key";
	private static final String DEFAULT_MESSAGE = "{0}:{1}为空，要求不为空。";

	public <T> void validate(String name, String title, T value,
			ValidateResult validateResult) {
		if (value == null) {
			validateResult.addError(name, i18nMessages.getMessage(NOT_NULL_VALIDATOR_MESSAGE_KEY,DEFAULT_MESSAGE,name, title));
		}
	}

}
