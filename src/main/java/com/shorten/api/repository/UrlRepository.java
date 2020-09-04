package com.shorten.api.repository;

import com.shorten.api.model.CountByDay;
import com.shorten.api.model.UrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Defined interface to the URLRepository JPA methods.
 */
@Repository
public interface UrlRepository extends JpaRepository<UrlEntity, Long> {

    /**
     * Find a Url entity using the shortUrl mapping
     *
     * @param shortUrl
     * @return Url entity using the shortUrl mapping
     */
    Optional<UrlEntity> findFirstByShortUrl(String shortUrl);

    /**
     * Find URL entity by it's longUrl mapping
     *
     * @param longUrl
     * @return URL entity by it's longUrl mapping
     */
    Optional<UrlEntity> findFirstByLongUrl(String longUrl);

    /**
     * Find the URL entities added within a given date interval.
     *
     * @param fromDate
     * @param toDate
     * @return the URL entities added within a given date interval.
     */
    List<UrlEntity> findAllByDateAddedBetween(LocalDate fromDate, LocalDate toDate);

    /**
     * Find the count of the URL entities added per day within a given date interval.
     *
     * @param fromDate
     * @param toDate
     * @return the count of the URL entities added per day within a given date interval.
     */
    @Query(value = "SELECT count(dateadded) as count, dateadded as dateadded from url_store WHERE dateadded >= ? and dateadded <= ? group by dateadded order by dateadded desc",
            nativeQuery = true)
    List<CountByDay> getAddedCountByDay(LocalDate fromDate, LocalDate toDate);

}