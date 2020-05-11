package com.log.analytics.com.log.analytics.entity;

import com.log.analytics.enums.RegionEnum;

import java.util.Date;

public class Log {import main.analytics.com.log.analytics.enums.RegionEnum;

    private String URL;
    private Date timestamp;
    private String userUUID;
    private RegionEnum region;

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserUUID() {
        return userUUID;
    }

    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
    }

    public RegionEnum getRegion() {
        return region;
    }

    public void setRegion(RegionEnum region) {
        this.region = region;
    }
}