package org.tinygroup.jdbctemplatedslsession;

import java.util.Map;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.tinygroup.jdbctemplatedslsession.ConfigurationBuilder.TableConfig;
import org.tinygroup.tinysqldsl.KeyGenerator;

/**
 * 配置对象
 * 
 * @author renhui
 *
 */
public class Configuration {
	// key:表名,value:表配置对象
	private Map<String, TableConfig> tableConfigMap = new CaseInsensitiveMap();
	// 默认的主键生成器
	private KeyGenerator defaultKeyGenerator;
	
	private boolean defaultAutoKeyGenerator=true;

	public KeyGenerator getDefaultKeyGenerator() {
		return defaultKeyGenerator;
	}

	public void setDefaultKeyGenerator(KeyGenerator defaultKeyGenerator) {
		this.defaultKeyGenerator = defaultKeyGenerator;
	}
	
	public boolean isDefaultAutoKeyGenerator() {
		return defaultAutoKeyGenerator;
	}

	public void setDefaultAutoKeyGenerator(boolean defaultAutoKeyGenerator) {
		this.defaultAutoKeyGenerator = defaultAutoKeyGenerator;
	}

	public void putTableConfig(TableConfig tableConfig) {
		tableConfigMap.put(tableConfig.getTableName(), tableConfig);
	}

	public Class getPojoClass(String tableName) {
		TableConfig tableConfig = tableConfigMap.get(tableName);
		if (tableConfig != null) {
			return tableConfig.getType();
		}
		return null;
	}

	public boolean isAutoGeneratedKeys(String tableName) {
		TableConfig tableConfig = tableConfigMap.get(tableName);
		if (tableConfig != null) {
			return tableConfig.isAutoGeneratedKeys();
		}
		return defaultAutoKeyGenerator;
	}
	
	public KeyGenerator getKeyGenerator(String tableName,String keyName){
		TableConfig tableConfig = tableConfigMap.get(tableName);
		KeyGenerator keyGenerator=null;
		if (tableConfig != null) {
			keyGenerator=tableConfig.getKeyGenerator(keyName);
		}
		if(keyGenerator==null){
			keyGenerator=defaultKeyGenerator;
		}
		return keyGenerator;
	}

}
