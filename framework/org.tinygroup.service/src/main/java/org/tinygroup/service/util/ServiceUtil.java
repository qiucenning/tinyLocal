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
package org.tinygroup.service.util;

import org.tinygroup.service.registry.ServiceRegistryItem;

import java.io.Serializable;
import java.util.Map;

public class ServiceUtil {

	public static boolean assignFromSerializable(Class<?> clazz){
		boolean isPrimitive=clazz.isPrimitive();
		if(isPrimitive){
			return true;
		}
		boolean isInterface=clazz.isInterface();
		if(isInterface){
			return true;
		}
		boolean isMapTypes=Map.class.isAssignableFrom(clazz);
		if(isMapTypes){
			return true;
		}
		if(clazz==Object.class){
			return true;
		}
		boolean seriaType=Serializable.class.isAssignableFrom(clazz);
		return seriaType;
	}
	
	public static ServiceRegistryItem copyServiceItem(ServiceRegistryItem serviceiItem) {
		ServiceRegistryItem item = new ServiceRegistryItem();
		item.setCategory(serviceiItem.getCategory());
		item.setDescription(serviceiItem.getDescription());
		item.setLocalName(serviceiItem.getLocalName());
		item.setParameters(serviceiItem.getParameters());
		item.setResults(serviceiItem.getResults());
		item.setService(serviceiItem.getService());
		return item;
	}
	
	
}
