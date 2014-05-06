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
package org.tinygroup.convert;

import java.util.List;

import org.tinygroup.convert.objectxml.jaxb.ObjectToXml;
import org.tinygroup.convert.objectxml.jaxb.XmlToObject;
import org.tinygroup.xmlparser.node.XmlNode;
import org.tinygroup.xmlparser.parser.XmlStringParser;

public class TestObjectXmlWithJAXB extends AbstractConvertTestCase{

	
	protected void setUp() throws Exception {
		super.setUp();
	}

	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testObject2Xml(){
		
		Classes classes=createClasses();
		ObjectToXml<Classes> objectToXml=new ObjectToXml<Classes>(Classes.class, true);
		String xml = objectToXml.convert(classes);
		XmlNode node= new XmlStringParser().parse(xml).getRoot();
		List<XmlNode> subNodes = node.getSubNodes("students");
	  	assertEquals(2, subNodes.size());
	  	XmlNode subNode= subNodes.get(0);
		assertEquals(subNode.getSubNode("id").getContent(), "1");
		assertEquals(subNode.getSubNode("name").getContent(), "haha");
		assertEquals(subNode.getSubNode("email").getContent(), "email");
	}
	
	public void testXml2Object(){
		String xml="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"+
  "<classes>"+
    "<students>"+
        "<address>address</address>"+
        "<birthday>"+
            "<birthday>2010-11-22</birthday>"+
        "</birthday>"+
        "<email>email</email>"+
        "<id>1</id>"+
        "<name>haha</name>"+
    "</students>"+
    "<students>"+
        "<address>address2</address>"+
        "<birthday>"+
            "<birthday>2010-11-22</birthday>"+
        "</birthday>"+
        "<email>email2</email>"+
        "<id>2</id>"+
        "<name>haha2</name>"+
    "</students>"+
   "</classes>";
		XmlToObject<Classes> xmlToObject=new XmlToObject<Classes>(Classes.class);
		Classes classes=xmlToObject.convert(xml);
		Student student=classes.getStudents().get(0);
    	assertEquals(2, classes.getStudents().size());
		assertEquals("haha", student.getName());
		assertEquals("email", student.getEmail());
		assertEquals("address", student.getAddress());
		assertEquals("2010-11-22", student.getBirthday().getBirthday());
	}

}
