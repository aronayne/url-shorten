package com.shorten.api.services;

import com.shorten.api.exception.*;
import com.shorten.api.model.URLShorten;
import com.shorten.api.model.UrlEntity;
import com.shorten.api.repositories.UrlRepository;
import com.shorten.api.system.Constants;
import com.shorten.api.validation.DateAddedValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class UrlShortenService {

    private Logger logger = LoggerFactory.getLogger(UrlShortenService.class);
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT);

    private UrlRepository urlRepository;
    private URLShorten urlShorten;
    private DateAddedValidator dateAddedValidator;

    @Autowired
    public UrlShortenService(UrlRepository urlRepository, URLShorten urlShorten, DateAddedValidator dateAddedValidator) {
        this.urlRepository = urlRepository;
        this.urlShorten = urlShorten;
        this.dateAddedValidator = dateAddedValidator;
    }

    /**
     * Find all URL entities added within a date interval.
     * @param fromDate
     * @param toDate
     * @return
     */
    public List<UrlEntity> findAllByDateAddedBetween(final String fromDate, final String toDate) {

        if (!dateAddedValidator.isValid(fromDate) || !dateAddedValidator.isValid(toDate)) {
            throw new InvalidDateException("The date is invalid");
        } else {
            LocalDate localFromDate = LocalDate.parse(fromDate, this.dateFormatter);
            LocalDate localToDate = LocalDate.parse(toDate, this.dateFormatter);

            logger.info("Finding URL's added between" + fromDate + " and " + toDate);
            return urlRepository.findAllByDateAddedBetween(localFromDate, localToDate);
        }
    }

    /**
     * Find a URL entity by it's id.
     * @param id
     * @return UrlEntity for a given id.
     */
    public Optional<UrlEntity> findById(final Long id) {
        return urlRepository.findById(id).map(Optional::of)
                .orElseThrow(() -> new LongUrlNotFoundException("URL not found for the given ID"));
    }

    /**
     * For a given shortUrl return it's corresponding long URL
     *
     * @param shortUrl
     * @return the long URL associated with a short URL.
     */
    public Optional<String> findByShortUrl(final String shortUrl) {
        return urlRepository.findFirstByShortUrl(shortUrl)
                .map(UrlEntity::getLongUrl)
                .map(Optional::of)
                .orElseThrow(() -> new ShortUrlNotFoundException("Long URL not found for the given short URL"));
    }

    /**
     * Save a URL entity
     * @param longUrl
     * @param dateAdded
     * @return the saved URL entity.
     */
    public UrlEntity saveUrlEntity(String longUrl, LocalDate dateAdded) {

        final int urlLength = longUrl.length();
        if (urlLength >= Constants.MAX_LONG_URL_LENGTH) {
            throw new LongUrlLengthExceededException("URL with length " + urlLength + " exceeds the max length of " + Constants.MAX_LONG_URL_LENGTH + " characters");
        }

        final List<UrlEntity> urlEntity = urlRepository.findByLongUrl(longUrl);
        if (!urlEntity.isEmpty()) {
            return urlEntity.get(0);
        }

        final String shortUrl = urlShorten.shortenURL(longUrl);
        if (urlRepository.findFirstByShortUrl(shortUrl).isPresent()) {
            logger.error("A short short URL collision occured for long URL: " + longUrl + " with generated short URL" + shortUrl);
            throw new ShortUrlCollisionException("A short URL collision occured");
        }

        logger.info("Shortened URL: " + shortUrl);
        final UrlEntity urlEntityToSave = new UrlEntity(dateAdded, longUrl, shortUrl);
        return urlRepository.save(urlEntityToSave);
    }

    private UrlEntity saveUrlEntityValue(String longUrl, LocalDate dateAdded){
        final String shortUrl = urlShorten.shortenURL(longUrl);
        if (urlRepository.findFirstByShortUrl(shortUrl).isPresent()) {
            logger.error("A short short URL collision occured for long URL: " + longUrl + " with generated short URL" + shortUrl);
            throw new ShortUrlCollisionException("A short URL collision occured");
        } else {
            logger.info("Shortened URL: " + shortUrl);

            final UrlEntity urlEntityToSave = new UrlEntity(dateAdded, longUrl, shortUrl);

            return urlRepository.save(urlEntityToSave);
        }
    }

}
