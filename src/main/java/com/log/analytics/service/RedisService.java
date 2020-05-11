package com.log.analytics.service;

import com.log.analytics.exceptions.NoDataFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Jedis;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RedisService {

    public static final String URL = "-URL";
    public static final String TIMESTAMP = "-TIMESTAMP";
    public static final String REGION = "-REGION";

    private Jedis redisClient;

    public RedisService(){
        init();
    }

    public boolean healthCheck(){
        String operation = redisClient.set("testKey", "testValue");
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

    public Set<String> getKeys(String keys){
        return this.redisClient.keys(keys);
    }

    public List<String> getValues(Stream<String> keys){
        return keys.map(key -> this.redisClient.get(key)).collect(Collectors.toList());
    }

    public Map.Entry<String, Integer> findMostCalledURLAndCount() throws NoDataFoundException{
        // Get all keys for URL pattern
        Stream<String> keys = getKeys("*"+URL).stream().sorted();
        List<String> values = getValues(keys);

        if(CollectionUtils.isEmpty(values))
            throw new NoDataFoundException(NoDataFoundException.NO_DATA_FOUND);

        HashMap<String, Integer> counts = new HashMap<>();

        // Counts and creates a hashmap with the count
        values.stream().distinct().forEach(s -> {
            counts.put(s, (int) values.stream().filter(filter -> filter.equals(s)).count());
        });

        //TODO: improve this code for better performance

        return counts.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).findFirst().get();
    }
}