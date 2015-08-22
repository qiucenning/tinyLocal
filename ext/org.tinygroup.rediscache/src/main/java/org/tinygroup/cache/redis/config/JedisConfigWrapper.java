package org.tinygroup.cache.redis.config;

import redis.clients.jedis.Protocol;

public class JedisConfigWrapper extends JedisConfig{

	private JedisConfig config;
	public JedisConfigWrapper(JedisConfig config){
		this.config = config;
	}
	
	public String getId() {
		return config.getId();
	}

	public String getCharset() {
		return config.getCharset()==null?"utf-8":config.getCharset();
	}

	public String getHost() {
		return config.getHost()==null?Protocol.DEFAULT_HOST:config.getHost();
	}

	public int getPort() {
		return config.getPort()<=0?Protocol.DEFAULT_PORT:config.getPort();
	}

	public int getTimeout() {
		return config.getTimeout()<0?Protocol.DEFAULT_TIMEOUT:config.getTimeout();
	}

	public String getPassword() {
		return config.getPassword();
	}

	public int getDatabase() {
		return config.getDatabase();
	}

	public String getClientName() {
		return config.getPassword();
	}

}
