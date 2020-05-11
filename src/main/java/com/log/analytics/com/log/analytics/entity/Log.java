package com.log.analytics.com.log.analytics.entity;

import com.log.analytics.enums.RegionEnum;

import java.util.Date;
import java.util.UUID;

public class Log {

    private String URL;
    private long timestamp;
    private UUID uuid;
    private RegionEnum region;

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public UUID getUUID() {
        return uuid;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public RegionEnum getRegion() {
        return region;
    }

    public void setRegion(RegionEnum region) {
        this.region = region;
    }

    public Date getTimeStampAsDate(){
        return new Date(this.timestamp);
    }
}