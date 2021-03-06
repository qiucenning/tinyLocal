/**
 * 
 */
package org.tinygroup.context2object.test.generator2.testcase;

import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.context2object.test.generator2.config.NoFieldObject;

/**
 * @author ywj
 *
 */
public class TestNoFieldObject extends BaseTestCast2 {

	public void testSimpleProperty(){
		Context context = new ContextImpl();
		context.put("bean.name", "name");
		context.put("bean.age", "11");
		NoFieldObject bean = (NoFieldObject) generator.getObject("bean", null,
				NoFieldObject.class.getName(), this.getClass().getClassLoader(),
				context);
		assertEquals("name",bean.getName());
		assertEquals(Integer.valueOf(11), bean.getAge());
	}
	
}
