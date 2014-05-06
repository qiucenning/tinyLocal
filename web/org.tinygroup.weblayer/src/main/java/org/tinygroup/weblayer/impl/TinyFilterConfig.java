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
package org.tinygroup.weblayer.impl;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;

import org.tinygroup.commons.tools.Enumerator;
import org.tinygroup.weblayer.listener.ServletContextHolder;

/**
 * FilterConfig的tiny实现
 * @author renhui
 *
 */
public class TinyFilterConfig implements FilterConfig {
	
	public static final String FILTER_BEAN_NAMES="filter_beans";
	
	private static final String WRAPPER_FILTER_NAME="wrapper_filter";
	
	private Map<String, String> initParams=new HashMap<String, String>();
	
	public TinyFilterConfig(Map<String, String> initParams){
		this.initParams=initParams;
	}

	public String getFilterName() {
		return WRAPPER_FILTER_NAME;
	}

	public ServletContext getServletContext() {
		 return ServletContextHolder.getServletContext();
	}

	public String getInitParameter(String name) {
		return initParams.get(name);
	}

	public Enumeration getInitParameterNames() {
		return new Enumerator(initParams.keySet());
	}

}
