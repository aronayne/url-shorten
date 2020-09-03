package com.shorten.api.system;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class Utilities {

    public static LocalDate getDate(String date) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT);
//        String date = "16/08/2016";

//convert String to LocalDate
        return LocalDate.parse(date, formatter);
//        String pattern = Constants.DATE_FORMAT;
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
//        try {
//            return Optional.of(simpleDateFormat.parse(date));
//        } catch (ParseException e) {
//            e.printStackTrace();
//            return Optional.empty();
//        }

    }

    public static Optional<Date> getTodayDate() {
        SimpleDateFormat sf = new SimpleDateFormat(Constants.DATE_FORMAT);
        try {
            return Optional.of(sf.parse(sf.format(new Date())));
        } catch (ParseException e) {
            e.printStackTrace();
            return Optional.empty();
        }

    }

    public static Date between(Date startInclusive, Date endExclusive) {
        long startMillis = startInclusive.getTime();
        long endMillis = endExclusive.getTime();
        long randomMillisSinceEpoch = ThreadLocalRandom.current().nextLong(startMillis, endMillis);

        return new Date(randomMillisSinceEpoch);
    }
}
