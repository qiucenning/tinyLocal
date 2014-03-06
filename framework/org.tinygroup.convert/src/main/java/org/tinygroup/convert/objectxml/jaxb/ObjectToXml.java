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
package org.tinygroup.convert.objectxml.jaxb;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.tinygroup.convert.Converter;

public class ObjectToXml<T> implements Converter<T, String> {
	JAXBContext context;
	private boolean format;
	Marshaller marshaller;

	public ObjectToXml(String className, boolean format) {
		try {
			context = JAXBContext.newInstance(className);
			marshaller = context.createMarshaller();
			this.format = format;
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}
	
	public ObjectToXml(Class<T> clazz,boolean format){
		try {
			context=JAXBContext.newInstance(clazz);
			marshaller = context.createMarshaller();
			this.format = format;
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}

	public String convert(T inputData) {

		StringWriter writer = new StringWriter();

		try {
			if (format) {
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
						Boolean.TRUE);
			}
			marshaller.marshal(inputData, writer);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return writer.toString();
	}

}
