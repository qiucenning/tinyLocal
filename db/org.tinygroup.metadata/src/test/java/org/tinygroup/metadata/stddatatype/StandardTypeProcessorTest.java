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
package org.tinygroup.metadata.stddatatype;

import junit.framework.TestCase;
import org.tinygroup.metadata.TestInit;
import org.tinygroup.metadata.util.MetadataUtil;
import org.tinygroup.springutil.SpringUtil;

public class StandardTypeProcessorTest extends TestCase {
	StandardTypeProcessor standardTypeProcessor;
	static {
		TestInit.init();
	}

	protected void setUp() throws Exception {
		super.setUp();
		standardTypeProcessor = SpringUtil.getBean(MetadataUtil.STANDARDTYPEPROCESSOR_BEAN);
	}

	public void testGetTypeStringStringString() {
//		assertEquals("varchar2(12)",
//				standardTypeProcessor.getType( "hsvarcharid", "oracle"));
//		assertEquals("varchar2(${length})",
//				standardTypeProcessor.getType( "hsvarchar1id", "oracle"));
	}

}
