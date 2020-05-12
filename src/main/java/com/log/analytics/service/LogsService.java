package com.log.analytics.service;

import com.log.analytics.com.log.analytics.entity.Log;
import com.log.analytics.com.log.analytics.entity.SingleMetric;
import com.log.analytics.enums.RegionEnum;
import com.log.analytics.exceptions.NoDataFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.Map.Entry;
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
            this.redisClient.putValue(log.getUUID().toString(), String.valueOf(log.getTimestamp()), log.getRegion().toString(), log.getURL());
        });
    }

    public List<SingleMetric> countURLsAccessed(RegionEnum region) throws NoDataFoundException {
        // Get all keys for URL pattern
        Stream<String> keys = redisClient.getKeys(parseRegion(region)).stream().sorted();
        List<String> values = redisClient.getValues(keys);

        if(CollectionUtils.isEmpty(values))
            throw new NoDataFoundException(NoDataFoundException.NO_DATA_FOUND);

        return values.stream().distinct()

                // Creates a list of SingleMetrics
                .map(s -> new SingleMetric()
                                .setKey(s)
                                .setCount((int) values.stream().filter(filter -> filter.equals(s)).count())
                                .setRegion(region)
                )
                .collect(Collectors.toList());
    }

    private String parseRegion(RegionEnum region){
        return region.equals(RegionEnum.ALL) ? "*" : "*|"+region.toString();
    }

    // Top 3 URL Accessed all around the world
    public List<SingleMetric> findMostURLsAccessed() throws NoDataFoundException {
        return countURLsAccessed(RegionEnum.ALL).stream()

                .sorted(Comparator.comparing(SingleMetric::getCount).reversed())

                // Get first three and convert to SingleMetric object
                .limit(3)
                .collect(Collectors.toList());
    }

    // The URL with less access in all world
    public SingleMetric findLeastURLAccessed() throws NoDataFoundException {
        return countURLsAccessed(RegionEnum.ALL).stream()

                // Sort per appearance count
                .sorted(Comparator.comparing(SingleMetric::getCount))

                // Get first and convert to SingleMetric object
                .limit(1)
                .findFirst().orElse(new SingleMetric());
    }

    // Top 3 URL Accessed per region
    public List<SingleMetric> findMostURLsAccessedPerRegion(RegionEnum region) throws NoDataFoundException {
        Stream<String> keys = redisClient.getKeys("*\\|"+region).stream().sorted();
        List<String> values = redisClient.getValues(keys);

        if(CollectionUtils.isEmpty(values))
            throw new NoDataFoundException(NoDataFoundException.NO_DATA_FOUND);

        return values.stream().distinct()
                // Creates a map <String, Integer> with <URL, CountItAppears>
                .map(s -> {
                    return new SingleMetric()
                                .setKey(s)
                                .setCount((int) values.stream().filter(filter -> filter.equals(s)).count())
                                .setRegion(region);
                })
                .sorted(Comparator.comparing(SingleMetric::getCount).reversed())
                .limit(3)
                .collect(Collectors.toList());
    }

    // Top 3 access per week
    public List<SingleMetric> findMostAccessedPerWeek() throws NoDataFoundException {
        return findMostAccessedPerTime(Calendar.WEEK_OF_YEAR, 3, RegionEnum.ALL);
    }

    // Top 3 access per day
    public List<SingleMetric> findMostAccessedPerDay() throws NoDataFoundException {
        return findMostAccessedPerTime(Calendar.DAY_OF_YEAR, 3, RegionEnum.ALL);
    }

    // Top 3 access per year
    public List<SingleMetric> findMostAccessedPerYear() throws NoDataFoundException {
        return findMostAccessedPerTime(Calendar.YEAR, 3, RegionEnum.ALL);
    }

    // The minute with more access in all URLs
    public int findMostAccessedPerMinute() throws NoDataFoundException{
        List<SingleMetric> registers = findMostAccessedPerTime(Calendar.MINUTE, 1, RegionEnum.ALL);

        return Integer.parseInt(registers.get(0).getKey());
    }

    public List<SingleMetric> findMostAccessedPerTime(int timeFormat, int topXResults, RegionEnum region) throws NoDataFoundException {
        // Get all keys for URL pattern
        List<String> keys = redisClient.getKeys("*").stream().sorted().collect(Collectors.toList());

        if(CollectionUtils.isEmpty(keys))
            throw new NoDataFoundException(NoDataFoundException.NO_DATA_FOUND);

        Calendar time = Calendar.getInstance();

        return keys.stream()
                // Creates a SingleMetric object with the metric (key), the count and the region
                .map(s -> {
                    long timestamp = Long.parseLong(s.split("\\|")[1]);
                    time.setTime(new Date(timestamp));

                    return new SingleMetric()
                                .setKey(String.valueOf(time.get(timeFormat)))
                                .setCount((int) keys.stream().filter(filter -> filter.equals(s)).count())
                                .setRegion(region);
                })

                .sorted(Comparator.comparing(SingleMetric::getCount).reversed())

                .limit(topXResults)
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