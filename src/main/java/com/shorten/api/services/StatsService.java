package com.shorten.api.services;

import com.shorten.api.model.CountByDay;
import com.shorten.api.model.StatsSummary;
import com.shorten.api.repositories.UrlRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class to the statistical operations.
 */
@Service
public class StatsService {

    private final UrlRepository urlRepository;
    private final com.shorten.api.stats.UrlShortenStatistics urlShortenStatistics;
    Logger logger = LoggerFactory.getLogger(StatsService.class);

    @Autowired
    public StatsService(UrlRepository urlRepository, com.shorten.api.stats.UrlShortenStatistics urlShortenStatistics) {
        this.urlRepository = urlRepository;
        this.urlShortenStatistics = urlShortenStatistics;
    }

    public List<CountByDay> getUrlCountByDay(final LocalDate dateFrom, final LocalDate dateTo) {

        logger.info("Finding count be day between dates " + dateFrom + " and " + dateTo);
        return urlRepository.getAddedCountByDay(dateFrom, dateTo);
    }

    public StatsSummary getStatsSummary(final LocalDate dateFrom, final LocalDate dateTo) {

        logger.info("Calculating summary stats between dates " + dateFrom + " and " + dateTo);
        List<CountByDay> countByDay = urlRepository.getAddedCountByDay(dateFrom, dateTo);
        List<Double> li = countByDay.stream().map(countDay -> Double.valueOf(countDay.getCount())).collect(Collectors.toList());
        Double mean = urlShortenStatistics.calculateMean(li);
        Double std = urlShortenStatistics.calculateStd(li);

        return new StatsSummary(mean, std);

    }

}
