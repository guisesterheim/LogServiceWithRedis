package com.log.analytics.service;

import com.log.analytics.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RedisService {

    public String redisHostname;

    public static final String TEST_KEY = "testKey";

    private Jedis redisClient;

    @Autowired
    public RedisService(@Value("${config.redis.hostname}") String hostname){
        this.redisHostname = hostname;
        init();
    }

    public boolean healthCheck(){
        String operation = redisClient.set(TEST_KEY, "testValue");
        return !operation.isEmpty() && !operation.isBlank();
    }

    public void init(){
        this.redisClient = new Jedis(this.redisHostname, 6379);

        System.out.println("hostname: "+this.redisHostname);
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

    public void putValue(String uuid, String timestamp, String region, String value){
        this.redisClient.set(uuid+"|"+timestamp+"|"+region, value);
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
