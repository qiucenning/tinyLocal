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
package org.tinygroup.beancontainer;

import org.tinygroup.exception.TinyBizRuntimeException;
import org.tinygroup.exception.errorcode.ErrorCodeDefault;

public class BeanContainerFactory {
	public static BeanContainer<?> container;

	public static void setBeanContainer(String beanClassName) {
		try {
			container = (BeanContainer) Class.forName(beanClassName)
					.newInstance();
		} catch (Exception e) {
			throw new TinyBizRuntimeException(ErrorCodeDefault.UNKNOWN_ERROR,
					e, beanClassName);
		}
	}

	public static BeanContainer<?> getBeanContainer(ClassLoader loader) {
		if (loader == BeanContainerFactory.class.getClassLoader()) {
			return container;
		} else {
			return container.getSubBeanContainer(loader);
		}

	}

	public static void removeBeanContainer(ClassLoader loader) {
		container.removeSubBeanContainer(loader);

	}
}
