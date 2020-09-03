package com.shorten.api.services;

import com.shorten.api.exception.LongUrlNotFoundException;
import com.shorten.api.exception.ShortUrlCollisionException;
import com.shorten.api.exception.UrlLengthExceededException;
import com.shorten.api.exception.UrlNotFoundException;
import com.shorten.api.model.URLShorten;
import com.shorten.api.model.UrlEntity;
import com.shorten.api.repositories.UrlRepository;
import com.shorten.api.system.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UrlService {

    Logger logger = LoggerFactory.getLogger(UrlService.class);

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private URLShorten urlCondenser;

    public List<UrlEntity> findAllByDateAddedBetween(final Date dateFrom, final Date dateTo) {

        logger.info("Finding URL's added between" + dateFrom + " and " + dateTo);
        return urlRepository.findAllByDateAddedBetween(dateFrom, dateTo);

//        SummaryResult sr = new SummaryResult("1", "0", ".5");

//        return sr;
    }

    public List<UrlEntity> findAll() {
        return urlRepository.findAll();
    }

    public Optional<UrlEntity> findById(final Long id) {
        return urlRepository.findById(id).map(Optional::of)
                .orElseThrow(() -> new UrlNotFoundException("URL not found for the given ID"));
    }

    /**
     * For a given shortUrl return it's corresponding longUrl
     *
     * @param shortUrl
     * @return
     */
    public Optional<String> findByShortUrl(final String shortUrl) {
        return urlRepository.findFirstByShortUrl(shortUrl)
                .map(UrlEntity::getLongUrl)
                .map(Optional::of)
                .orElseThrow(() -> new LongUrlNotFoundException("Long URL not found for the given short URL"));
    }

    /**
     * @param longUrl
     * @return
     *
     * TODO: refactor this with a helper class for validation.
     */
    public UrlEntity saveUrlEntity(String longUrl, Date dateAdded) {

        int urlLength = longUrl.length();
        if (urlLength >= Constants.MAX_LONG_URL_LENGTH) {
            throw new UrlLengthExceededException("URL with length " + urlLength + " exceeds the max length of " + Constants.MAX_LONG_URL_LENGTH + " characters");
        } else {

            List<UrlEntity> urlEntity = urlRepository.findByLongUrl(longUrl);
            if (urlEntity.size() > 0) {
                return urlEntity.get(0);
            } else {
                final String shortUrl = urlCondenser.shortenURL(longUrl);
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
    }

}
