package com.shorten.api.service;

import com.shorten.api.exception.ExceptionMessage;
import com.shorten.api.exception.InvalidDateException;
import com.shorten.api.model.CountByDay;
import com.shorten.api.model.StatsSummary;
import com.shorten.api.repository.UrlRepository;
import com.shorten.api.stats.UrlShortenStatistics;
import com.shorten.api.system.Constants;
import com.shorten.api.validation.DateAddedValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class to the statistical operations.
 */
@Service
public class StatsService {

    private final UrlRepository urlRepository;
    private final DateAddedValidator dateAddedValidator;
    private final UrlShortenStatistics urlShortenStatistics;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT);

    Logger logger = LoggerFactory.getLogger(StatsService.class);

    @Autowired
    public StatsService(UrlRepository urlRepository, UrlShortenStatistics urlShortenStatistics,
                        DateAddedValidator dateAddedValidator) {
        this.urlRepository = urlRepository;
        this.urlShortenStatistics = urlShortenStatistics;
        this.dateAddedValidator = dateAddedValidator;
    }

    /**
     * Calculate the count of distinct long URL's added within a Date interval.
     * @param fromDate
     * @param toDate
     * @return count of distinct long URL's added within a Date interval.
     */
    public List<CountByDay> getUrlCountByDay(final String fromDate, final String toDate) {

        if (!dateAddedValidator.isValid(fromDate) || !dateAddedValidator.isValid(toDate)) {
            throw new InvalidDateException(ExceptionMessage.INVALID_DATE.message);
        } else {
            LocalDate localFromDate = LocalDate.parse(fromDate, this.dateFormatter);
            LocalDate localToDate = LocalDate.parse(toDate, this.dateFormatter);

            logger.info("Finding count be day between dates " + fromDate + " and " + toDate);
            return urlRepository.getAddedCountByDay(localFromDate, localToDate);
        }

    }

    /**
     * Calculate the mean and standard deviation of distinct long URL's added within a Date interval.
     * @param dateFrom
     * @param dateTo
     * @return mean and standard deviation of distinct long URL's added within a Date interval.
     */
    public StatsSummary getStatsSummary(final String dateFrom, final String dateTo) {

        if (!dateAddedValidator.isValid(dateFrom) || !dateAddedValidator.isValid(dateTo)) {
            throw new InvalidDateException(ExceptionMessage.INVALID_DATE.message);
        } else {
            logger.info("Calculating summary stats between dates " + dateFrom + " and " + dateTo);

            LocalDate localFromDate = LocalDate.parse(dateFrom, this.dateFormatter);
            LocalDate localToDate = LocalDate.parse(dateTo, this.dateFormatter);
            List<CountByDay> countByDay = urlRepository.getAddedCountByDay(localFromDate, localToDate);
            List<Double> li = countByDay.stream().map(countDay -> Double.valueOf(countDay.getCount())).collect(Collectors.toList());
            Double mean = urlShortenStatistics.calculateMean(li);
            Double std = urlShortenStatistics.calculateStd(li);

            return new StatsSummary(mean, std);
        }

    }

}
