package com.shorten.api.model;

/**
 *
 */
public class StatsSummary {

    private Double meanPerDay;
    private Double stdPerDay;

    public StatsSummary(Double mean, Double std) {
        this.meanPerDay = mean;
        this.stdPerDay = std;
    }

    public Double getMeanPerDay() {
        return meanPerDay;
    }

    public Double getStdPerDay() {
        return stdPerDay;
    }

}
