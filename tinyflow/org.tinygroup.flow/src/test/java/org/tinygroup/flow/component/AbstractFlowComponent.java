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
package org.tinygroup.flow.component;

import junit.framework.TestCase;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.flow.FlowExecutor;
import org.tinygroup.tinytestutil.AbstractTestUtil;

public abstract class AbstractFlowComponent extends TestCase {

	protected FlowExecutor flowExecutor;
	protected FlowExecutor pageFlowExecutor;

	void init() {
		AbstractTestUtil.init("application.xml", true);
	}

	protected void setUp() throws Exception {
		init();
		flowExecutor = BeanContainerFactory.getBeanContainer(
				this.getClass().getClassLoader()).getBean(
				FlowExecutor.FLOW_BEAN);
		pageFlowExecutor = BeanContainerFactory.getBeanContainer(
				this.getClass().getClassLoader()).getBean(
				FlowExecutor.PAGE_FLOW_BEAN);
	}

	public void testDemo() {
	}

}
