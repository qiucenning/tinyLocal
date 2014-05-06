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
package org.tinygroup.binarytree;

import org.tinygroup.binarytree.impl.BinaryTreeImpl;
import org.tinygroup.commons.processor.Processor;

import junit.framework.TestCase;

public class BinaryTreeTest extends TestCase {

	BinaryTreeImpl<Integer> binTree;

	
	protected void setUp() throws Exception {
		binTree = new BinaryTreeImpl<Integer>(10);
		super.setUp();
	}

	
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testHeight() {
		binTree.add(9);
		binTree.add(8);
		binTree.add(7);
		binTree.add(6);
		binTree.foreach(new ProcessItem());
		assertEquals(5, binTree.height());
	}

}

class ProcessItem implements Processor<Integer> {

	public void process(Integer object) {
		System.out.println(object);
	}

}
