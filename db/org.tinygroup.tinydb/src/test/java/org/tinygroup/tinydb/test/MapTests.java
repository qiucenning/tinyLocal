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
package org.tinygroup.tinydb.test;

import org.tinygroup.tinydb.relation.Relation;
import org.tinygroup.tinytestutil.AbstractTestUtil;
import org.tinygroup.xstream.XStreamFactory;

import com.thoughtworks.xstream.XStream;

public class MapTests {
	public static void main(String[] args) {
//		Map<String,String> map = new HashMap<String, String>();
//		map.put("aa", null);
//		System.out.println(map.containsKey("aa"));
		ssds();
	}
	static void ssds(){
		AbstractTestUtil.init(null, true);
		Relation branchRelation=new Relation("233","branch","", "id", "");
		Relation relation=new Relation("11", "animal", Relation.LIST_MODE, "id", Relation.MORE_TO_ONE,branchRelation);
		
		XStream xStream=XStreamFactory.getXStream("tinydb");
		System.out.println(xStream.toXML(relation));
		
	}
}
