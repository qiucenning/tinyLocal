package org.tinygroup.cepcorenetty;

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcore.EventProcessor;
import org.tinygroup.cepcore.EventProcessorRegisterTrigger;
import org.tinygroup.cepcorenetty.operator.ArOperator;

public class ArReRegisterTrigger implements EventProcessorRegisterTrigger {

	public void trigger(EventProcessor processor, CEPCore core) {
		ArOperator operator = (ArOperator)core.getOperator();
//		operator.reRun();
	}

}