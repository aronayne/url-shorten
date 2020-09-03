package com.shorten.api.controllers;

import com.shorten.api.exception.InvalidDateException;
import com.shorten.api.model.UrlEntity;
import com.shorten.api.services.UrlShortenService;
import com.shorten.api.system.Constants;
import com.shorten.api.validation.DateAddedValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * Manages requests for URL shortening functionality
 */
@RestController
public class UrlShortenController {

    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT);

    private final UrlShortenService urlShortenService;

    private Logger logger = LoggerFactory.getLogger(UrlShortenController.class);

    @Autowired
    public UrlShortenController(UrlShortenService urlShortenService) {
        this.urlShortenService = urlShortenService;
    }

    /**
     * Return the UrlEntity by it's ID.
     * @param id
     * @return UrlEntity
     */
    @GetMapping("/shorten/{id}")
    public ResponseEntity<UrlEntity> retrieveUrlById(@PathVariable Long id) {

        final Optional<UrlEntity> url = urlShortenService.findById(id);

        if (url.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(url.get(), HttpStatus.OK);
        }

    }

    /**
     * Redirect a browser client to the long url for a given short url.
     * @param shortUrl
     * @return
     */
    @GetMapping("/shorten/redirect/{shortUrl}")
    ResponseEntity<?> redirect(@PathVariable String shortUrl) {

        final Optional<String> url = urlShortenService.findByShortUrl(shortUrl);

        if (url.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            String longUrl = url.get();
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(longUrl))
                    .build();
        }
    }

    /**
     * Find all entities created within a Date time interval.
     * @param fromDate
     * @param toDate
     * @return all entities created within a Date time interval
     */
    @GetMapping("/shorten/created/{fromDate}/{toDate}")
    public List<UrlEntity> findAllByDateAddedBetween(@PathVariable String fromDate, @PathVariable String toDate) {

        logger.info("Finding long URL(s) created from dates " + fromDate + " to " + toDate);

         return urlShortenService.findAllByDateAddedBetween(fromDate, toDate);

      //  }
    }

    /**
     * Create a shortened URL from a long URL
     *
     * @param longUrl
     * @return shortened URL
     */
    @PutMapping("/shorten/")
    public ResponseEntity<String> createShortenedUrl(@RequestParam @NotBlank @Max(2000) String longUrl) {

        logger.info("Long URL to convert is " + longUrl);

            final LocalDate dateAdded = LocalDate.now();
            UrlEntity savedUrlEntity = urlShortenService.saveUrlEntity(longUrl, dateAdded);

            String shortUrl = savedUrlEntity.getShortUrl();
            logger.info("Converted Long URL is " + shortUrl);

            return new ResponseEntity<String>(shortUrl, HttpStatus.OK);

    }
}

