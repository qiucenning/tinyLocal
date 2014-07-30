package org.tinygroup.cepcorepc.test.regex;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcorepc.test.aop.util.AopTestUtil;
import org.tinygroup.cepcorepc.test.synchronous.Service;
import org.tinygroup.context.util.ContextFactory;
import org.tinygroup.event.Event;
import org.tinygroup.event.ServiceRequest;
import org.tinygroup.tinytestutil.AbstractTestUtil;

public class TestCase extends junit.framework.TestCase {
	
	public void test() {
		AbstractTestUtil.init(null, true);
		CEPCore cep = BeanContainerFactory.getBeanContainer(
				AopTestUtil.class.getClassLoader()).getBean(
				CEPCore.CEP_CORE_BEAN);
		
		RegexProcessor p = new RegexProcessor();
		Service s1 = new Service("a1");
		Service s2 = new Service("a2");
		p.addService(s1);
		p.addService(s2);
		cep.registerEventProcessor(p);

		Event e = new Event();
		ServiceRequest s = new ServiceRequest();
		e.setServiceRequest(s);
		s.setContext(ContextFactory.getContext());
		s.setServiceId("a3");
		e.setMode(Event.EVENT_MODE_SYNCHRONOUS);
		
		
		try {
			cep.process(e);
			assertTrue(true);
		} catch (Exception e2) {
			assertTrue(false);
		}
		
		
	}
}