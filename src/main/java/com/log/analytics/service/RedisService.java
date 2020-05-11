package com.log.analytics.service;

import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RedisService {

    public static final String URL = "-URL";
    public static final String TIMESTAMP = "-TIMESTAMP";
    public static final String REGION = "-REGION";
    public static final String TEST_KEY = "testKey";

    private Jedis redisClient;

    public RedisService(){
        init();
    }

    public boolean healthCheck(){
        String operation = redisClient.set(TEST_KEY, "testValue");
        return !operation.isEmpty() && !operation.isBlank();
    }

    public void init(){
        this.redisClient = new Jedis("localhost", 6379);

        System.out.println(this.redisClient.isConnected());

        /*
            Optional implementation for future

            Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
            jedisClusterNodes.add(new HostAndPort("127.0.0.1", 7379));
            JedisCluster jc = new JedisCluster(jedisClusterNodes);
         */
    }

    public void finish(){
        this.redisClient.save();
    }

    private void putValue(String key, String value){
        this.redisClient.set(key, value);
    }

    public void putURL(String uuid, String value){
        this.putValue(uuid+URL, value);
    }

    public void putTimestamp(String uuid, String value){
        this.putValue(uuid+TIMESTAMP, value);
    }

    public void putRegion(String uuid, String value){
        this.putValue(uuid+REGION, value);
    }

    public String getValue(String key){
        return this.redisClient.get(key);
    }

    public Set<String> getKeys(String keys){
        return this.redisClient.keys(keys);
    }

    public List<String> getValues(Stream<String> keys){
        return keys.map(key -> this.redisClient.get(key)).collect(Collectors.toList());
    }
}
