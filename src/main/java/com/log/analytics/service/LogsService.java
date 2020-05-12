package com.log.analytics.service;

import com.log.analytics.com.log.analytics.entity.Log;
import com.log.analytics.com.log.analytics.entity.SingleMetric;
import com.log.analytics.exceptions.NoDataFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.swing.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.prefs.NodeChangeEvent;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class LogsService {

    @Autowired
    private RedisService redisClient;

    private Comparator<Entry<String, Integer>> mapValueStringComparator;
    private Comparator<Entry<Integer, Integer>> mapValueIntegerComparator;

    public boolean healthCheck(){
        return this.redisClient.healthCheck();
    }

    public void ingestLogs(List<Log> logs){
        logs.forEach(log -> {
            this.redisClient.putURL(log.getUUID().toString(), log.getURL());
            this.redisClient.putTimestamp(log.getUUID().toString(), String.valueOf(log.getTimestamp()));
            this.redisClient.putRegion(log.getUUID().toString(), log.getRegion().toString());
        });
    }

    public List<Entry<String, Integer>> countURLsAccessed() throws NoDataFoundException {
        // Get all keys for URL pattern
        Stream<String> keys = redisClient.getKeys("*"+RedisService.URL).stream().sorted();
        List<String> values = redisClient.getValues(keys);

        if(CollectionUtils.isEmpty(values))
            throw new NoDataFoundException(NoDataFoundException.NO_DATA_FOUND);

        return values.stream().distinct()
                .map(s -> {
                    return new AbstractMap.SimpleEntry<String, Integer>(s, (int) values.stream().filter(filter -> filter.equals(s)).count());
                }).collect(Collectors.toList());
    }

    // Top 3 URL Accessed all around the world
    public List<SingleMetric> findMostURLsAccessed() throws NoDataFoundException {
        return countURLsAccessed().stream()
                .sorted(getStringComparator().reversed())
                .limit(3)
                .map(entry -> new SingleMetric()
                        .setMostCalledURL(entry.getKey())
                        .setMostCalledURLCount(entry.getValue()))
                .collect(Collectors.toList());
    }

    // The URL with less access in all world
    public SingleMetric findLeastURLAccessed() throws NoDataFoundException {
        return countURLsAccessed().stream()
                .sorted(getStringComparator())
                .limit(1)
                .map(entry -> new SingleMetric()
                        .setMostCalledURL(entry.getKey())
                        .setMostCalledURLCount(entry.getValue()))
                .findFirst().orElse(new SingleMetric());
    }

    // TODO: Top 3 URL Accessed per region
    public List<Entry<String, Integer>> findMostURLsAccessedPerRegion() throws NoDataFoundException {
        Stream<String> keys = redisClient.getKeys("*"+RedisService.REGION).stream().distinct().sorted();



        List<String> values = redisClient.getValues(keys);

        System.out.println(keys);

        return null;
    }


    // TODO: Top 3 access per day, week, year


    // TODO: The minute with more access in all URLs
    public int findMostAccessedMinute() throws NoDataFoundException {
        // Get all keys for URL pattern
        Stream<String> keys = redisClient.getKeys("*"+RedisService.TIMESTAMP).stream().sorted();
        List<String> values = redisClient.getValues(keys);

        if(CollectionUtils.isEmpty(values))
            throw new NoDataFoundException(NoDataFoundException.NO_DATA_FOUND);

        Calendar time = Calendar.getInstance();

        //Map<Integer, Integer> ret =
        return values.stream().distinct()

            // Creates a map <Integer, Integer> with <Minute, CountItAppears>
            .map(s -> {
                time.setTime(new Date(Long.parseLong(s)));
                return new AbstractMap.SimpleEntry<Integer, Integer>(time.get(Calendar.MINUTE), (int) values.stream().filter(filter -> filter.equals(s)).count());
            })

            // Collect to summarize duplicate minutes
            .collect(Collectors.groupingBy(entry -> entry.getKey(), Collectors.summingInt(entry -> entry.getValue())))
            .entrySet().stream()

            // Sorts to get the biggest count on top
            .sorted(getIntegerComparator().reversed())

            .limit(1)
            .mapToInt(entry -> entry.getKey()).findFirst().orElse(-1);
    }

    private Comparator<Entry<String, Integer>> getStringComparator(){
        if(mapValueStringComparator == null)
            mapValueStringComparator = new Comparator<Entry<String,Integer>>() {
                @Override public int compare(Entry<String, Integer> e1, Entry<String, Integer> e2) {
                    return e1.getValue().compareTo(e2.getValue());
                }
            };

        return mapValueStringComparator;
    }

    private Comparator<Entry<Integer, Integer>> getIntegerComparator(){
        if(mapValueIntegerComparator == null)
            mapValueIntegerComparator = new Comparator<Entry<Integer,Integer>>() {
                @Override public int compare(Entry<Integer, Integer> e1, Entry<Integer, Integer> e2) {
                    return e1.getValue().compareTo(e2.getValue());
                }
            };

        return mapValueIntegerComparator;
    }
}