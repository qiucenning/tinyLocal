/**
 *  Copyright (c) 1997-2013, www.tinygroup.org (luo_guo@icloud.com).
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
package org.tinygroup.springutil.test.beancontainer;

import junit.framework.TestCase;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.tinygroup.springutil.SpringBeanContainer;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.VFS;

import java.util.ArrayList;
import java.util.List;

public class BeanCopyTest extends TestCase {

	public void testContainer() {
		SpringBeanContainer sbc = new SpringBeanContainer();
		FileObject f = VFS.resolveURL(this.getClass().getClassLoader()
				.getResource("beancontainer.beans.xml"));
		List<FileObject> fl = new ArrayList<FileObject>();
		fl.add(f);
		sbc.regSpringConfigXml(fl);
		sbc.refresh();
		FileSystemXmlApplicationContext fileSystemXmlApplicationContext = (FileSystemXmlApplicationContext) sbc
				.getBeanContainerPrototype();
		DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) fileSystemXmlApplicationContext
				.getBeanFactory();
		BeanDefinition beanDefinition = defaultListableBeanFactory
				.getBeanDefinition("containerbean");
		BeanDefinitionBuilder builder = BeanDefinitionBuilder
				.rootBeanDefinition(beanDefinition.getBeanClassName());
		MutablePropertyValues attributes = beanDefinition.getPropertyValues();
		builder.setScope("singleton");
		for (Object value : attributes.getPropertyValueList()) {
			PropertyValue propertyValue = (PropertyValue) value;
			builder.addPropertyValue(propertyValue.getName(),
					propertyValue.getValue());
		}
		assertNotNull(beanDefinition);
		System.out.println(defaultListableBeanFactory);
		defaultListableBeanFactory.registerBeanDefinition("aaa",builder.getBeanDefinition());
		assertNotSame( fileSystemXmlApplicationContext.getBean("aaa"), fileSystemXmlApplicationContext.getBean("containerbean"));
		
	}
}
