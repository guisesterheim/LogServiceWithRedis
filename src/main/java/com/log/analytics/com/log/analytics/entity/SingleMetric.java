package com.log.analytics.com.log.analytics.entity;

import com.log.analytics.enums.RegionEnum;

public class SingleMetric {

    private int count;
    private String key;
    private RegionEnum region;

    public int getCount() {
        return count;
    }

    public SingleMetric setCount(int count) {
        this.count = count;
        return this;
    }

    public String getKey() {
        return key;
    }

    public SingleMetric setKey(String key) {
        this.key = key;
        return this;
    }

    public SingleMetric setRegion(RegionEnum region){
        this.region = region;
        return this;
    }

    public RegionEnum getRegion() {
        return region;
    }
}
