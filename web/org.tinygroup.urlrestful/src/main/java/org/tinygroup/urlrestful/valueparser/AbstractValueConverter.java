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
package org.tinygroup.urlrestful.valueparser;

import org.springframework.beans.factory.InitializingBean;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.urlrestful.ValueConverter;
import org.tinygroup.urlrestful.handler.RestfulStyleSubstitutionHandler;

/**
 * 
 * @author renhui
 * 
 */
public abstract class AbstractValueConverter implements ValueConverter,
		InitializingBean {

	public void afterPropertiesSet() throws Exception {
		RestfulStyleSubstitutionHandler handler = BeanContainerFactory
				.getBeanContainer(getClass().getClassLoader()).getBean(
						HANDLER_BEAN);
		handler.addConvert(this);
	}
}
