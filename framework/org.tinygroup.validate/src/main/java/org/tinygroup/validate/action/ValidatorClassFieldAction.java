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
package org.tinygroup.validate.action;

import org.tinygroup.annotation.AnnotationPropertyAction;
import org.tinygroup.validate.AnnotationValidatorManager;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * 符合@Validation注解类的处理器
 * @author renhui
 *
 */
public class ValidatorClassFieldAction implements AnnotationPropertyAction {
	
	private AnnotationValidatorManager annotationValidatorManager;
	
	
	public AnnotationValidatorManager getAnnotationValidatorManager() {
		return annotationValidatorManager;
	}


	public void setAnnotationValidatorManager(
			AnnotationValidatorManager annotationValidatorManager) {
		this.annotationValidatorManager = annotationValidatorManager;
	}



	public <T> void process(Class<T> clazz, Field field, Annotation annotation) {
		
		annotationValidatorManager.addValidatorAnnotation(clazz, field, annotation);
	}

}
