package com.shorten.api.controllers;

import com.shorten.api.model.CountByDay;
import com.shorten.api.model.StatsSummary;
import com.shorten.api.model.URLShorten;
import com.shorten.api.repositories.UrlRepository;
import com.shorten.api.services.StatsService;
import com.shorten.api.system.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * Manages requests for the statistical functions.
 */
@RestController
public class StatisticsController {

    Logger logger = LoggerFactory.getLogger(StatisticsController.class);

    private StatsService statsService;

    @Autowired
    public StatisticsController(StatsService statsService) {
        this.statsService = statsService;
    }

    /**
     * Calculate per day a statistics summary of items added within a Date range.
     * @param from
     * @param to
     * @return the mean, standard deviation
     */
    @GetMapping("/stats/{from}/{to}")
    public StatsSummary getStatsSummary(@DateTimeFormat(pattern = Constants.DATE_FORMAT) @PathVariable Date from,
                                        @DateTimeFormat(pattern = Constants.DATE_FORMAT) @PathVariable Date to) {

        logger.info("From" + from + ",to" + to);

        return statsService.getStatsSummary(from, to);

    }

    /**
     * Calculate teh number of distinct long url's added per day.
     * @param from
     * @param to
     * @return
     */
    @GetMapping("/stats/addedCountByDay/{from}/{to}")
    public List<CountByDay> getUrlCountbyDay(@DateTimeFormat(pattern = Constants.DATE_FORMAT) @PathVariable Date from,
                                             @DateTimeFormat(pattern = Constants.DATE_FORMAT) @PathVariable Date to) {

        logger.info("addedCountByDay" + from + ",to" + to);

        return statsService.getUrlCountByDay(from, to);

    }

}

