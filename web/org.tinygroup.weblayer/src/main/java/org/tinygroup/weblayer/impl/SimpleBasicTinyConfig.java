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
package org.tinygroup.weblayer.impl;

import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.weblayer.BasicTinyConfig;
import org.tinygroup.weblayer.config.BasicConfigInfo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SimpleBasicTinyConfig implements BasicTinyConfig {

	protected String configName;

	protected Map<String, String> parameterMap = new HashMap<String, String>();

	protected static Logger logger = LoggerFactory
			.getLogger(SimpleBasicTinyConfig.class);

	public SimpleBasicTinyConfig(String configName,
			BasicConfigInfo basicConfigInfo) {
		super();
		this.configName = configName;
		this.parameterMap.putAll(basicConfigInfo.getParameterMap());
	}

	public SimpleBasicTinyConfig(List<BasicConfigInfo> basicConfigInfos) {
		super();
		this.configName = "compositeConfig";
		for (BasicConfigInfo basicConfigInfo : basicConfigInfos) {
			this.parameterMap.putAll(basicConfigInfo.getParameterMap());
		}
	}

	public String getConfigName() {
		return configName;
	}

	public String getInitParameter(String name) {
		return parameterMap.get(name);
	}

	public Iterator<String> getInitParameterNames() {
		return parameterMap.keySet().iterator();
	}

	public Map<String, String> getParameterMap() {
		return parameterMap;
	}
}
