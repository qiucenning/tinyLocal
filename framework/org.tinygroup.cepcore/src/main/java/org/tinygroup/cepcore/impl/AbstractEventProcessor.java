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
package org.tinygroup.cepcore.impl;

import org.tinygroup.cepcore.EventProcessor;

/**
 * 抽象的事件处理器实现
 * @author renhui
 * 
 */
public abstract class AbstractEventProcessor implements EventProcessor {
	
	private boolean enable = true;
	
	public String getId() {
		return this.getClass().getSimpleName();
	}

	public int getType() {
		return EventProcessor.TYPE_LOCAL;
	}
	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

}
