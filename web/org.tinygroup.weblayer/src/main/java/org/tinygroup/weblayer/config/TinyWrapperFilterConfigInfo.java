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
package org.tinygroup.weblayer.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.tinygroup.commons.tools.Assert;
import org.tinygroup.commons.tools.StringUtil;

@XStreamAlias("tiny-wrapper-filter")
public class TinyWrapperFilterConfigInfo extends TinyFilterConfigInfo {

	private static String FILTER_BEAN_NAME = "filter_beans";

	public String getFilterBeanName() {
		String filterBean = getParameterMap().get(FILTER_BEAN_NAME);
		Assert.assertTrue(!StringUtil.isBlank(filterBean),
				"the filter_beans property of value must not null or empty");
		return filterBean;
	}

}
