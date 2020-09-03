package com.shorten.api.controllers;

import com.shorten.api.model.UrlEntity;
import com.shorten.api.repositories.UrlRepository;
import com.shorten.api.services.UrlShortenService;
import com.shorten.api.system.Constants;
import com.shorten.api.system.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Manages requests for URL shortening functionality
 */
@RestController
public class UrlShortenController {

    private final UrlShortenService urlShortenService;
    private final UrlRepository urlRepository;
    Logger logger = LoggerFactory.getLogger(UrlShortenController.class);

    @Autowired
    public UrlShortenController(UrlShortenService urlShortenService, UrlRepository urlRepository) {
        this.urlShortenService = urlShortenService;
        this.urlRepository = urlRepository;
    }

    /**
     * Return the url entity by it's ID.
     * @param id
     * @return
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
    ResponseEntity<Void> redirect(@PathVariable String shortUrl) {

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
     * Find all entities creaeted within a Date time interval.
     * @param from
     * @param to
     * @return
     */
    @GetMapping("/shorten/created/{from}/{to}")
    public List<UrlEntity> findAllByDateAddedBetween(@DateTimeFormat(pattern = Constants.DATE_FORMAT) @PathVariable Date from,
                                   @DateTimeFormat(pattern = Constants.DATE_FORMAT) @PathVariable Date to) {

        logger.info("Finding long URL(s) created from dates " + from + " to " + to);

        return urlShortenService.findAllByDateAddedBetween(from, to);
    }

    /**
     * Create a shortened Url from a long Url
     *
     * @param longUrl
     * @return shortened Url
     */
    @PutMapping("/shorten/")
    public ResponseEntity<?> createShortenedUrl(@RequestParam String longUrl) {

        logger.info("Long URL to convert is " + longUrl);

            final Date dateAdded = Utilities.getTodayDate().get();
            UrlEntity savedUrlEntity = urlShortenService.saveUrlEntity(longUrl, dateAdded);

            String shortUrl = savedUrlEntity.getShortUrl();
            logger.info("Converted Long URL is " + shortUrl);

            return new ResponseEntity<>(shortUrl, HttpStatus.OK);

    }
}

