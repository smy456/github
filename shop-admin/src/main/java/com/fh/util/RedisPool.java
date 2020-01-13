package com.fh.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisPool {
    private static JedisPool jedisPool;
    public static void initPool(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(1000);    //最大连接数, 默认8个
        jedisPoolConfig.setMinIdle(100);      //最小空闲连接数，默认0
        jedisPoolConfig.setMaxIdle(100);      //最大空闲连接数，默认8个
        jedisPoolConfig.setTestOnReturn(true);//在获取连接的时候检查有效性, 默认false
        jedisPoolConfig.setTestOnBorrow(true);//在空闲时检查有效性, 默认false
        jedisPool = new JedisPool(jedisPoolConfig, "192.168.254.128", 6379);

    }
    static{
        initPool();
    }
    public static Jedis getResources(){
        return jedisPool.getResource();
    }
}
