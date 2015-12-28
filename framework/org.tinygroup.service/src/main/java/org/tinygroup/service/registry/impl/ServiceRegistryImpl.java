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
package org.tinygroup.service.registry.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.service.Service;
import org.tinygroup.service.registry.ServiceRegistry;
import org.tinygroup.service.registry.ServiceRegistryItem;

public class ServiceRegistryImpl implements ServiceRegistry {
	/**
	 * 指定服务ID，默认服务MAP
	 */
	private final Map<String, ServiceRegistryItem> serviceIdMap = new HashMap<String, ServiceRegistryItem>();

	private final Map<Service, ServiceRegistryItem> serviceToServiceRegistryItem = new HashMap<Service, ServiceRegistryItem>();
	private static Logger logger = LoggerFactory
			.getLogger(ServiceRegistryImpl.class);

	public void registeService(ServiceRegistryItem serviceRegistryItem) {
		if(serviceIdMap.containsKey(serviceRegistryItem.getServiceId())){
			throw new RuntimeException("服务:"+serviceRegistryItem.getServiceId()+"已存在，不允许存在相同id服务");
		}
		logger.logMessage(LogLevel.INFO, "添加服务[serviceId:{0}]",
				serviceRegistryItem.getServiceId());
		serviceIdMap.put(serviceRegistryItem.getServiceId(),
				serviceRegistryItem);
		serviceToServiceRegistryItem.put(serviceRegistryItem.getService(),
				serviceRegistryItem);

	}

	public void registeService(List<ServiceRegistryItem> serviceRegistryItems) {
		for (ServiceRegistryItem serviceRegistryItem : serviceRegistryItems) {
			registeService(serviceRegistryItem);
		}
	}

	public void registeService(ServiceRegistryItem[] serviceRegistryItems) {
		for (ServiceRegistryItem serviceRegistryItem : serviceRegistryItems) {
			registeService(serviceRegistryItem);
		}
	}

	public void registeService(Set<ServiceRegistryItem> serviceRegistryItems) {
		for (ServiceRegistryItem serviceRegistryItem : serviceRegistryItems) {
			registeService(serviceRegistryItem);
		}
	}

	public void removeService(String serviceId) {
		removeService(serviceIdMap.get(serviceId));

	}

	private void removeService(ServiceRegistryItem serviceRegistryItem) {
		if (serviceRegistryItem == null) {
			return;
		}
		logger.logMessage(LogLevel.INFO, "删除服务[serviceId:{0}]",
				serviceRegistryItem.getServiceId());
		serviceIdMap.remove(serviceRegistryItem.getServiceId());
		serviceToServiceRegistryItem.remove(serviceRegistryItem.getService());
	}

	public int size() {
		return serviceToServiceRegistryItem.size();
	}

	public void clear() {
		serviceIdMap.clear();
		serviceToServiceRegistryItem.clear();
	}

	public ServiceRegistryItem getServiceRegistryItem(Service service) {
		return serviceToServiceRegistryItem.get(service);
	}

	public ServiceRegistryItem getServiceRegistryItem(String serviceId) {
		return serviceIdMap.get(serviceId);
	}

	public Collection<ServiceRegistryItem> getServiceRegistryItems() {
		return serviceIdMap.values();
	}

}
