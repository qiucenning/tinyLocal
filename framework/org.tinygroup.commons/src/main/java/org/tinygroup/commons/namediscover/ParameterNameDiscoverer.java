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
package org.tinygroup.commons.namediscover;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * 
 * @author renhui
 *
 */
public interface ParameterNameDiscoverer {
	
	/**
	 * Return parameter names for this method,
	 * or <code>null</code> if they cannot be determined.
	 * @param method method to find parameter names for
	 * @return an array of parameter names if the names can be resolved,
	 * or <code>null</code> if they cannot
	 */
	String[] getParameterNames(Method method);
	
	/**
	 * Return parameter names for this constructor,
	 * or <code>null</code> if they cannot be determined.
	 * @param ctor constructor to find parameter names for
	 * @return an array of parameter names if the names can be resolved,
	 * or <code>null</code> if they cannot
	 */
	String[] getParameterNames(Constructor ctor);

}
