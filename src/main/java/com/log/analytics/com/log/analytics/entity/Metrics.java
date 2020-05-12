package com.log.analytics.com.log.analytics.entity;

import java.util.List;

public class Metrics {

    List<SingleMetric> mostAccessedURLsWorldwide;
    SingleMetric lessAccessedURLWorldWide;
    int mostAccessedMinute;

    public List<SingleMetric> getMostAccessedURLsWorldwide() {
        return mostAccessedURLsWorldwide;
    }

    public void setMostAccessedURLsWorldwide(List<SingleMetric> mostAccessedURLsWorldwide) {
        this.mostAccessedURLsWorldwide = mostAccessedURLsWorldwide;
    }

    public SingleMetric getLessAccessedURLWorldWide() {
        return lessAccessedURLWorldWide;
    }

    public void setLessAccessedURLWorldWide(SingleMetric lessAccessedURLWorldWide) {
        this.lessAccessedURLWorldWide = lessAccessedURLWorldWide;
    }

    public int getMostAccessedMinute() {
        return mostAccessedMinute;
    }

    public void setMostAccessedMinute(int mostAccessedMinute) {
        this.mostAccessedMinute = mostAccessedMinute;
    }
}
