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

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.convert.objectxml.simple.ObjectToXml;
import org.tinygroup.convert.objectxml.simple.XmlToObject;
import org.tinygroup.xmlparser.node.XmlNode;
import org.tinygroup.xmlparser.parser.XmlStringParser;

public class TestObjectXmlWithSimple extends AbstractConvertTestCase {

	public void testObject2Xml(){
		Student student1=createStudent();
		Student student2=createStudent1();
		List<Student> students=new ArrayList<Student>();
		students.add(student1);
		students.add(student2);
		ObjectToXml<Student> objectToXml=new ObjectToXml<Student>(true, "students", "student");
	  	String xml = objectToXml.convert(students);
	  	XmlNode node= new XmlStringParser().parse(xml).getRoot();
	  	List<XmlNode> subNodes = node.getSubNodes("student");
	  	assertEquals(2, subNodes.size());
	  	XmlNode studentNode = subNodes.get(0);
	  	assertEquals(studentNode.getAttribute("id"), "1");
		assertEquals(studentNode.getAttribute("name"), "haha");
		assertEquals(studentNode.getAttribute("email"), "email");
	}
	
	
	public void testXml2Object(){
		
		String xml="<students>" +
				        "<student  id=\"1\" name=\"haha\" email=\"email\" " +
				        "      address=\"address\"/>" +
				        "<student  id=\"2\" name=\"haha2\" email=\"email2\" " +
				        "       address=\"address2\"/>" +
				     "</students>";
		XmlToObject<Student> xmlToObject=new XmlToObject<Student>(Student.class.getName(), "students", "student");
		List<Student> students=xmlToObject.convert(xml);
		Student student=students.get(0);
    	assertEquals(2, students.size());
		assertEquals("haha", student.getName());
		assertEquals("email", student.getEmail());
		assertEquals("address", student.getAddress());
	}
}
