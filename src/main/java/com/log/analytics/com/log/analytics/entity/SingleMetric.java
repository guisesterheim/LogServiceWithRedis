package com.log.analytics.com.log.analytics.entity;

public class SingleMetric {

    private int mostCalledURLCount;
    private String mostCalledURL;

    public int getMostCalledURLCount() {
        return mostCalledURLCount;
    }

    public SingleMetric setMostCalledURLCount(int mostCalledURLCount) {
        this.mostCalledURLCount = mostCalledURLCount;
        return this;
    }

    public String getMostCalledURL() {
        return mostCalledURL;
    }

    public SingleMetric setMostCalledURL(String mostCalledURL) {
        this.mostCalledURL = mostCalledURL;
        return this;
    }
}
