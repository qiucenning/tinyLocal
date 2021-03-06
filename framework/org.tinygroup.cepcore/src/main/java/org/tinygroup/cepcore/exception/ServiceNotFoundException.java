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
package org.tinygroup.cepcore.exception;

import org.tinygroup.cepcoreexceptioncode.CEPCoreExceptionCode;
import org.tinygroup.exception.BaseRuntimeException;

public class ServiceNotFoundException extends BaseRuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9094597551672712176L;
	private final String serviceId;

	public String getServiceId() {
		return serviceId;
	}

	public ServiceNotFoundException(String serviceId) {
		//TODO:此处serviceID被当成了defaultMessage
		super(CEPCoreExceptionCode.SERVICE_NOT_FOUND_EXCEPTION_CODE,serviceId);
		this.serviceId = serviceId;
	}

}
