package org.tinygroup.redis.test.shard;

import junit.framework.Assert;

import org.tinygroup.jedis.ShardJedisSentinelManager;
import org.tinygroup.jedis.shard.TinyShardJedis;

import redis.clients.jedis.Jedis;

public class ThreadNew2 implements Runnable {
	ShardJedisSentinelManager manager ;
	int times;
	
	public ThreadNew2(ShardJedisSentinelManager manager,int times) {
		this.manager = manager;
		this.times = times;
	}

	public void run() {
		for (int i = 0; i < times; i++) {
			TinyShardJedis shardedJedis = manager.getShardedJedis();
			testReadAndWrite(manager,shardedJedis, "WriteAndRead");
			manager.returnResource(shardedJedis);
		}
		System.out.println("=========================END================================");
	}
	

	private void testReadAndWrite(ShardJedisSentinelManager manager,TinyShardJedis shardedJedis,
			String key) {
		System.out.println("----------------------");
		// 只有这个种情况，是对读服务器进行服务器进行操作
				Jedis read = shardedJedis.getReadShard(key);
		// shardedJedis.getShard获得的链接操作数据和直接通过TinyShardJedis是一样
		Jedis write = shardedJedis.getShard(key);
		//因为在write后拿read，所以read与wirte一样
		Jedis read2 = shardedJedis.getReadShard(key);
		System.out.println(write);
		System.out.println(read);
		Assert.assertNotSame(write, read);
		Assert.assertSame(write, read2);
		System.out.println("----------------------");
	}
}
