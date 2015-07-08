/**
 *  Copyright (c) 1997-2013, www.tinygroup.org (tinygroup@126.com).
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
package org.tinygroup.cepcoreimpl.test;

import org.tinygroup.cepcore.EventProcessor;
import org.tinygroup.event.Event;

public class CepCoreProcessTestCast extends CEPCoreBaseTestCase {

	public void setUp() {	
		super.setUp();
		init() ;
	}

	private void init() {
		EventProcessor eventProcessor = new EventProcessorForTest();
		eventProcessor.getServiceInfos().add(initServiceInfo("a"));
		eventProcessor.getServiceInfos().add(initServiceInfo("b"));
		eventProcessor.getServiceInfos().add(initServiceInfo("exception"));
		getCore().registerEventProcessor(eventProcessor);
	}

	public void testAy() {
		Event e = getEvent("a");
		e.setMode(Event.EVENT_MODE_ASYNCHRONOUS);
		getCore().process(e);
	}

	public void testy() {
		Event e = getEvent("a");
		e.setMode(Event.EVENT_MODE_SYNCHRONOUS);
		getCore().process(e);
	}
	
	public void testException() {
		Event e = getEvent("exception");
		e.setMode(Event.EVENT_MODE_SYNCHRONOUS);
		try {
			getCore().process(e);
		} catch (Exception e2) {
			assertEquals(e2.getMessage(), "testExceptionhandler");
		}
		
	}

}
