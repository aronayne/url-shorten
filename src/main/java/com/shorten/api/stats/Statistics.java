package com.shorten.api.stats;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Statistics {

    public static void main(String[] args) {

        Statistics s = new Statistics();
        List<Double> l = new ArrayList<Double>();
        l.add(5.0);
        l.add(10.0);
        l.add(15.0);
        System.out.println(s.calculateMean(l));
        System.out.println(s.calculateStd(l));
//        //Stadnard deviation proivides an indication of how the number of saved links per hour deviate from the mean.
//        double[] valuesAddedPerHourFor1Day = {11, 2, 3, 420, 5, 6, 7, 82, 9, 1220, 11, 122, 13, 214, 15, 160, 17, 128, 19, 20, 1, 2, 3, 03};
//        double SD = calculateSD(valuesAddedPerHourFor1Day);
//        double m1 = calculateMean(valuesAddedPerHourFor1Day);
//
//        double[] valuesAddedPerHourFor1Day2 = {1, 2, 3, 42, 5, 6, 7, 82, 9, 10, 11, 12, 13, 4, 15, 10, 17, 8, 1, 2, 1, 2, 3, 3};
//        double SD2 = calculateSD(valuesAddedPerHourFor1Day2);
//        double m2 = calculateMean(valuesAddedPerHourFor1Day2);
//
//
//        double[] valuesAddedPerHourFor1Day3 = {100, 200, 300, 4200, 500, 600, 700, 8200, 900, 1000, 1100, 1200, 1300, 400, 1500, 1000, 1700, 800, 100, 200, 100, 200, 300, 300};
//        double SD3 = calculateSD(valuesAddedPerHourFor1Day3);
//        double m3 = calculateMean(valuesAddedPerHourFor1Day3);
//
//        System.out.format("\n Standard Deviation = %.6f", SD);
//        System.out.format("\n Mean = %.6f", m1);
//
//        System.out.format("\n Standard Deviation = %.6f", SD2);
//        System.out.format("\n Mean = %.6f", m2);
//
//        System.out.format("\n Standard Deviation = %.6f", SD3);
//        System.out.format("\n Mean = %.6f", m3);
//
//
//        List<Double> list11 = Arrays.stream(valuesAddedPerHourFor1Day3).boxed().collect(Collectors.toList());
//
////        List<Double> list = new ArrayList<Double>(Arrays.asList(valuesAddedPerHourFor1Day3));
//
//        Double m4 = calculateMean(list11);
//        System.out.format("\n Mean = %.6f", m4);
//
    }

    public Double calculateMean(List<Double> values) {

        Double valuesSum = values.stream().mapToDouble(Double::doubleValue).sum();

        return valuesSum / values.size();
    }

    public double calculateStd(List<Double> values) {

        int precision = 3;
        Double mean = calculateMean(values);
        Double differenceSquared = Double.valueOf(0);

//        Double valuesSum = values.stream().mapToDouble(Double::intValue).sum();

        for (Double temp : values) {
            differenceSquared += Math.pow(temp - mean, 2);
        }

        Double std = Math.sqrt(differenceSquared / values.size());

        return Math.round(std * Math.pow(10, precision)) / Math.pow(10, precision);
    }


}
