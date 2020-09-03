package com.shorten.api.stats;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Implements various statistical calculations
 */
@Component
public class UrlShortenStatistics implements Statistics {

    /**
     * Calculate the mean for List of value Double values
     *
     * @param values
     * @return the mean for List of value Double values
     */
    public Double calculateMean(List<Double> values) {

        Double valuesSum = values.stream().mapToDouble(Double::doubleValue).sum();

        return valuesSum / values.size();
    }

    /**
     * Calculate the standard deviation for a List of Double values
     *
     * @param values
     * @return the standard deviation for a List of Double values
     */
    public Double calculateStd(List<Double> values) {

        int precision = 3;
        Double mean = calculateMean(values);
        Double differenceSquared = Double.valueOf(0);

        for (Double temp : values) {
            differenceSquared += Math.pow(temp - mean, 2);
        }

        Double std = Math.sqrt(differenceSquared / values.size());

        return Math.round(std * Math.pow(10, precision)) / Math.pow(10, precision);
    }


}
