package com.log.analytics.service;

import redis.clients.jedis.Jedis;

public class RedisService {

    private Jedis redisClient;

    public void init(){
        this.redisClient = new Jedis("localhost");
        /*
            Optional implememntation for future

            Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
            jedisClusterNodes.add(new HostAndPort("127.0.0.1", 7379));
            JedisCluster jc = new JedisCluster(jedisClusterNodes);
         */
    }

    public void setValue(String key, String value){

    }

}
