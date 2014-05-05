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
package org.tinygroup.parser.filter;

import org.tinygroup.commons.processor.Processor;
import org.tinygroup.parser.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FastNameFilter<T extends Node<T>> extends AbstractFilterImpl<T> {
	private Map<String, List<T>> nodeTable = null;

	class NodeAdd implements Processor<T> {
		public void process(T node) {
			if (node.getNodeName() != null) {
				List<T> vector = nodeTable.get(node.getNodeName());
				if (vector == null) {
					vector = new ArrayList<T>();
					nodeTable.put(node.getNodeName(), vector);
				}
				vector.add(node);
			}
		}
	}

	public void init(T node) {
		super.init(node);
		nodeTable = new HashMap<String, List<T>>();
		node.foreach(new NodeAdd());

	}

	public FastNameFilter() {

	}

	public FastNameFilter(T node) {
		init(node);
	}

	public List<T> findNodeList(String nodeName) {
		List<T> result = nodeTable.get(nodeName);
		if (result != null) {
			result = filteNode(result);
		} else {
			result = new ArrayList<T>();
		}
		return result;
	}

}
