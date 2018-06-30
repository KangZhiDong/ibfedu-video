package com.ibf.live.common.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisPool {

    private JedisPoolConfig config; 
    private String serverIp;
    private int port;
    private JedisPool pool;

    public void init() {
        pool = new JedisPool(config, serverIp, port, 4000);
    }

    public Jedis getInstance() {
        return pool.getResource();
    }

    public void returnResource(Jedis jedis) {
        pool.returnResource(jedis);
    }

    public void returnBrokenResource(Jedis jedis){
        pool.returnBrokenResource(jedis);
    }

    public JedisPoolConfig getConfig() {
        return config;
    }

    public void setConfig(JedisPoolConfig config) {
        this.config = config;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}

