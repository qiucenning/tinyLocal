package org.tinygroup.remoteconfig.zk.client;

import java.util.Map;

import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.remoteconfig.RemoteConfigManageClient;
import org.tinygroup.remoteconfig.config.ConfigPath;
import org.tinygroup.remoteconfig.manager.ConfigItemManager;


public class ConfigItemManagerImpl implements ConfigItemManager ,RemoteConfigManageClient{

	protected static final Logger LOGGER = LoggerFactory
			.getLogger(ConfigItemManagerImpl.class);
	
	public boolean exists(String key ,ConfigPath configPath){
		return ZKManager.exists(key, configPath);
	}

	public String get(String key ,ConfigPath configPath){
		return ZKManager.get(key, configPath);
	}

	public Map<String, String> getAll(ConfigPath configPath) {
		return ZKManager.getAll(configPath);
	}

	public void start() {
	}

	public void stop() {
		ZKManager.stop();
	}

	public void delete(String key, ConfigPath configPath) {
		ZKManager.delete(key, configPath);
	}

	public void set(String key, String value, ConfigPath configPath) {
		ZKManager.set(key, value, configPath);
		
	}

}
