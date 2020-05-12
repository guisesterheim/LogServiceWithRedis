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
        // TODO: change ingestion to add "UUID+timestamp+region" as key and URL as valueo.

        logs.forEach(log -> {
            this.redisClient.putValue(log.getUUID().toString(), String.valueOf(log.getTimestamp()), log.getRegion().toString(), log.getURL());
        });
    }

    public List<Entry<String, Integer>> countURLsAccessed() throws NoDataFoundException {
        // Get all keys for URL pattern
        Stream<String> keys = redisClient.getKeys("*").stream().sorted();
        List<String> values = redisClient.getValues(keys);

        if(CollectionUtils.isEmpty(values))
            throw new NoDataFoundException(NoDataFoundException.NO_DATA_FOUND);

        return values.stream().distinct()

                // Creates a map <String, Integer> with <URL, CountItAppears>
                .map(s -> {
                    return new AbstractMap.SimpleEntry<String, Integer>(s, (int) values.stream().filter(filter -> filter.equals(s)).count());
                }).collect(Collectors.toList());
    }

    // Top 3 URL Accessed all around the world
    public List<SingleMetric> findMostURLsAccessed() throws NoDataFoundException {
        return countURLsAccessed().stream()

                // Sort per appearance count
                .sorted(getStringComparator().reversed())

                // Get first three and convert to SingleMetric object
                .limit(3)
                .map(entry -> new SingleMetric()
                        .setMostCalledURL(entry.getKey())
                        .setMostCalledURLCount(entry.getValue()))
                .collect(Collectors.toList());
    }

    // The URL with less access in all world
    public SingleMetric findLeastURLAccessed() throws NoDataFoundException {
        return countURLsAccessed().stream()

                // Sort per appearance count
                .sorted(getStringComparator())

                // Get first and convert to SingleMetric object
                .limit(1)
                .map(entry -> new SingleMetric()
                        .setMostCalledURL(entry.getKey())
                        .setMostCalledURLCount(entry.getValue()))
                .findFirst().orElse(new SingleMetric());
    }

    // TODO: Top 3 URL Accessed per region
    public List<Entry<String, Integer>> findMostURLsAccessedPerRegion() throws NoDataFoundException {
        Stream<String> keys = redisClient.getKeys("*").stream().distinct().sorted();

        List<String> values = redisClient.getValues(keys);

        return null;
    }

    // Top 3 access per week
    public List<SingleMetric> findMostAccessedPerWeek() throws NoDataFoundException {
        return mapToSingleMetrics(findMostAccessedPerTime(Calendar.WEEK_OF_YEAR, 3));
    }

    // Top 3 access per day
    public List<SingleMetric> findMostAccessedPerDay() throws NoDataFoundException {
        return mapToSingleMetrics(findMostAccessedPerTime(Calendar.DAY_OF_YEAR, 3));
    }

    // Top 3 access per year
    public List<SingleMetric> findMostAccessedPerYear() throws NoDataFoundException {
        return mapToSingleMetrics(findMostAccessedPerTime(Calendar.YEAR, 3));
    }

    // The minute with more access in all URLs
    public int findMostAccessedPerMinute() throws NoDataFoundException{
        List<Map.Entry<String, Integer>> registers = findMostAccessedPerTime(Calendar.MINUTE, 1);

        return Integer.parseInt(registers.get(0).getKey());
    }

    public List<Map.Entry<String, Integer>> findMostAccessedPerTime(int timeFormat, int topXResults) throws NoDataFoundException {
        // Get all keys for URL pattern
        List<String> keys = redisClient.getKeys("*").stream().sorted().collect(Collectors.toList());

        if(CollectionUtils.isEmpty(keys))
            throw new NoDataFoundException(NoDataFoundException.NO_DATA_FOUND);

        Calendar time = Calendar.getInstance();

        //Map<Integer, Integer> ret =
        return keys.stream().distinct()

            // Creates a map <Integer, Integer> with <TimeFormat (Week, Minute, Day or Year), CountItAppears>
            .map(s -> {
                long timestamp = Long.parseLong(s.split("\\|")[1]);
                time.setTime(new Date(timestamp));

                return new AbstractMap.SimpleEntry<Integer, Integer>(time.get(timeFormat), (int) keys.stream().filter(filter -> filter.equals(s)).count());
            })

            // Collect to summarize duplicate minutes
            .collect(Collectors.groupingBy(entry -> entry.getKey(), Collectors.summingInt(entry -> entry.getValue())))
            .entrySet().stream()

            // Sorts to get the biggest count on top
            .sorted(getIntegerComparator().reversed())

            .limit(topXResults)
            .map(entry -> new AbstractMap.SimpleEntry<String, Integer>(String.valueOf(entry.getKey()), entry.getValue()))
            .collect(Collectors.toList());
    }

    private List<SingleMetric> mapToSingleMetrics(List<Entry<String, Integer>> registersPerWeek) {
        return registersPerWeek.stream()
                .map(entry -> new SingleMetric().setMostCalledURL(entry.getKey()).setMostCalledURLCount(entry.getValue()))
                .collect(Collectors.toList());
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