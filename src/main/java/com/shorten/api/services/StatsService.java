package com.shorten.api.services;

import com.shorten.api.model.CountByDay;
import com.shorten.api.model.StatsSummary;
import com.shorten.api.repositories.UrlRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatsService {

    private final UrlRepository urlRepository;
    private final com.shorten.api.stats.Statistics statistics;
    Logger logger = LoggerFactory.getLogger(StatsService.class);

    @Autowired
    public StatsService(UrlRepository urlRepository, com.shorten.api.stats.Statistics statistics) {
        this.urlRepository = urlRepository;
        this.statistics = statistics;
    }

    public List<CountByDay> getUrlCountByDay(final Date dateFrom, final Date dateTo) {

        logger.info("Finding count be day between dates " + dateFrom + " and " + dateTo);
        return urlRepository.getAddedCountByDay(dateFrom, dateTo);
    }

    public StatsSummary getStatsSummary(final Date dateFrom, final Date dateTo) {

        logger.info("Calculating summary stats between dates " + dateFrom + " and " + dateTo);
        List<CountByDay> countByDay = urlRepository.getAddedCountByDay(dateFrom, dateTo);
        List<Double> li = countByDay.stream().map(countDay -> Double.valueOf(countDay.getCount())).collect(Collectors.toList());
        Double mean = statistics.calculateMean(li);
        Double std = statistics.calculateStd(li);

        return new StatsSummary(mean, std);

    }

}
