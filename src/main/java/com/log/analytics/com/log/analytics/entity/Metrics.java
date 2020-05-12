package com.log.analytics.com.log.analytics.entity;

import java.util.List;

public class Metrics {

    List<SingleMetric> mostAccessedURLsWorldwide;
    List<SingleMetric> mostAccessedURLPerRegion;
    SingleMetric lessAccessedURLWorldWide;
    int mostAccessedMinute;
    List<SingleMetric> topThreeAccessPerDay;
    List<SingleMetric> topThreeAccessPerWeek;
    List<SingleMetric> topThreeAccessPerYear;

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

    public List<SingleMetric> getMostAccessedURLPerRegion() {
        return mostAccessedURLPerRegion;
    }

    public void setMostAccessedURLPerRegion(List<SingleMetric> mostAccessedURLPerRegion) {
        this.mostAccessedURLPerRegion = mostAccessedURLPerRegion;
    }

    public List<SingleMetric> getTopThreeAccessPerDay() {
        return topThreeAccessPerDay;
    }

    public void setTopThreeAccessPerDay(List<SingleMetric> topThreeAccessPerDay) {
        this.topThreeAccessPerDay = topThreeAccessPerDay;
    }

    public List<SingleMetric> getTopThreeAccessPerWeek() {
        return topThreeAccessPerWeek;
    }

    public void setTopThreeAccessPerWeek(List<SingleMetric> topThreeAccessPerWeek) {
        this.topThreeAccessPerWeek = topThreeAccessPerWeek;
    }

    public List<SingleMetric> getTopThreeAccessPerYear() {
        return topThreeAccessPerYear;
    }

    public void setTopThreeAccessPerYear(List<SingleMetric> topThreeAccessPerYear) {
        this.topThreeAccessPerYear = topThreeAccessPerYear;
    }
}
