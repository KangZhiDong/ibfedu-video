package com.ibf.live.common.redis;

import redis.clients.jedis.Jedis;

public class RedisManage {

    private  RedisPool redisPool;

    public  Jedis getJedis() {
		 return redisPool.getInstance();
    }

    public  void releaseJedis(Jedis jedis) {
        redisPool.returnResource(jedis);
    }

    public  void releaseBrokenJedis(Jedis jedis) {
        redisPool.returnBrokenResource(jedis);
    }
    
    public  RedisPool getRedisPool() {
        return redisPool;
    }

    public  void setRedisPool(RedisPool redisPool) {
        this.redisPool = redisPool;
    }

    /**
     * SCAN 命令用于迭代当前数据库中的数据库键。
     * SSCAN 命令用于迭代集合键中的元素。
     * HSCAN 命令用于迭代哈希键中的键值对。
      *ZSCAN 命令用于迭代有序集合中的元素（包括元素成员和元素分值）
     * @param key
     * @return
     */
}
