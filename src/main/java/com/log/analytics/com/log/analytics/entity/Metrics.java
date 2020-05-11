package com.log.analytics.com.log.analytics.entity;

public class Metrics {

    private int mostCalledURLCount;
    private String mostCalledURL;

    public int getMostCalledURLCount() {
        return mostCalledURLCount;
    }

    public Metrics setMostCalledURLCount(int mostCalledURLCount) {
        this.mostCalledURLCount = mostCalledURLCount;
        return this;
    }

    public String getMostCalledURL() {
        return mostCalledURL;
    }

    public Metrics setMostCalledURL(String mostCalledURL) {
        this.mostCalledURL = mostCalledURL;
        return this;
    }
}
