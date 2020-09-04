package com.shorten.api.controller;

import com.shorten.api.model.CountByDay;
import com.shorten.api.model.StatsSummary;
import com.shorten.api.service.StatsService;
import com.shorten.api.system.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
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
     *
     * @param fromDate
     * @param toDate
     * @return the mean, standard deviation
     */
    @GetMapping("/stats/{fromDate}/{toDate}")
    public StatsSummary getStatsSummary(@PathVariable String fromDate, @PathVariable String toDate) {

        logger.info("Calculating statistics between" + fromDate + ",to" + toDate);

        return statsService.getStatsSummary(fromDate, toDate);

    }

    /**
     * Calculate the number of distinct long URL's added per day.
     *
     * @param fromDate
     * @param toDate
     * @return the number of distinct long URL's added per day.
     */
    @GetMapping("/stats/addedCountByDay/{fromDate}/{toDate}")
    public List<CountByDay> getUrlCountbyDay(@PathVariable String fromDate, @PathVariable String toDate) {

        logger.info("Calculating the number of distinct long URL's added by day between" + fromDate + ",to" + toDate);

        return statsService.getUrlCountByDay(fromDate, toDate);

    }

}

