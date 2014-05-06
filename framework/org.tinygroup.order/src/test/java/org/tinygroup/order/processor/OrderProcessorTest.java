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
package org.tinygroup.order.processor;

import java.util.List;

import junit.framework.TestCase;

import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.order.orderObject.NeedOrder;
import org.tinygroup.order.orderObject.One;
import org.tinygroup.order.orderObject.Three;
import org.tinygroup.order.orderObject.Two;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.VFS;

public class OrderProcessorTest extends TestCase {
	
	
	
	protected void setUp() throws Exception {
		
	}
	
	
	public void testOrder(){
		OrderProcessor<NeedOrder> processor=new OrderProcessor<NeedOrder>();
		FileObject fileObject = null;
		fileObject = VFS.resolveFile(getClass().getResource("/test.order.xml").getPath());
		processor.loadOrderFile(fileObject);
		List<NeedOrder> orders=CollectionUtil.createArrayList();
		orders.add(new Three());
		orders.add(new One());
		orders.add(new Two());
		assertEquals(3, orders.get(0).getPosition());
		List<NeedOrder> afterOrderList= processor.orderList("needOrder", orders);
		assertEquals(1, afterOrderList.get(0).getPosition());
		
		
	}

	
}
