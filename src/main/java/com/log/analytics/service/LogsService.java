package com.log.analytics.service;

import com.log.analytics.com.log.analytics.entity.Log;
import com.log.analytics.exceptions.NoDataFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.InfoProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Jedis;

import javax.swing.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class LogsService {

    @Autowired
    private RedisService redisClient;

    public void ingestLogs(List<Log> logs){
        logs.forEach(log -> {
            this.redisClient.putURL(log.getUUID().toString(), log.getURL());
            this.redisClient.putTimestamp(log.getUUID().toString(), String.valueOf(log.getTimestamp()));
            this.redisClient.putRegion(log.getUUID().toString(), log.getRegion().toString());
        });
    }

    public Map.Entry<String, Integer> findMostCalledURLAndCount() throws NoDataFoundException{
        // Get all keys for URL pattern
        Stream<String> keys = redisClient.getKeys("*"+RedisService.URL).stream().sorted();
        List<String> values = redisClient.getValues(keys);

        if(CollectionUtils.isEmpty(values))
            throw new NoDataFoundException(NoDataFoundException.NO_DATA_FOUND);

        Map<String, Integer> counts = values.stream().distinct().map(s -> {
            return new AbstractMap.SimpleEntry<String, Integer>(s, (int) values.stream().filter(filter -> filter.equals(s)).count());
        }).collect(Collectors.toMap(simpleEntry1 -> simpleEntry1.getKey(), simpleEntry2 -> simpleEntry2.getValue()));

        //TODO: improve this code for better performance

        return counts.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).findFirst().get();
    }

    public boolean healthCheck(){
        return this.redisClient.healthCheck();
    }
}