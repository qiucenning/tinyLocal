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
package org.tinygroup.flowbasiccomponent;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.context.Context;
import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.tinydb.Bean;
import org.tinygroup.tinydb.BeanOperatorManager;
import org.tinygroup.tinydb.exception.TinyDbException;
import org.tinygroup.tinydb.operator.DBOperator;
import org.tinygroup.tinydb.util.TinyDBUtil;

public abstract class AbstractTinydbService implements ComponentInterface {

	
	protected String beanType;
	protected String resultKey;
	protected String schema;
	
	protected Logger logger=LoggerFactory.getLogger(TinydbAddService.class);

	public String getBeanType() {
		return beanType;
	}

	public void setBeanType(String beanType) {
		this.beanType = beanType;
	}
	
	

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getResultKey() {
		return resultKey;
	}

	public void setResultKey(String resultKey) {
		this.resultKey = resultKey;
	}
	
	public void execute(Context context) {
		BeanOperatorManager manager=BeanContainerFactory.getBeanContainer(
				this.getClass().getClassLoader()).getBean(BeanOperatorManager.OPERATOR_MANAGER_BEAN);
		try {
			DBOperator operator = manager.getDbOperator(schema);
			Bean bean=TinyDBUtil.context2Bean(context, beanType,schema,this.getClass().getClassLoader());
			tinyService(bean,context, operator);
		} catch (TinyDbException e) {
			throw new RuntimeException(e);
		}
	}
	
	public abstract void tinyService(Bean bean,Context context,DBOperator operator);
	

	
}
