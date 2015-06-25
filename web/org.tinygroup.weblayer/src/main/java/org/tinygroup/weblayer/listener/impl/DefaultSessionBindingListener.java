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
package org.tinygroup.weblayer.listener.impl;

import org.tinygroup.weblayer.listener.TinySessionBindingListener;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

public class DefaultSessionBindingListener extends SimpleBasicTinyConfigAware
		implements TinySessionBindingListener {

	private HttpSessionBindingListener sessionBindingListener;

	public DefaultSessionBindingListener(
			HttpSessionBindingListener sessionBindingListener) {
		super();
		this.sessionBindingListener = sessionBindingListener;
	}

	public void valueBound(HttpSessionBindingEvent event) {
		sessionBindingListener.valueBound(event);
	}

	public void valueUnbound(HttpSessionBindingEvent event) {
		sessionBindingListener.valueUnbound(event);
	}

	public int getOrder() {
		return DEFAULT_PRECEDENCE;
	}

}
