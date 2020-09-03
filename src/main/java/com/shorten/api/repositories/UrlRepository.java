package com.shorten.api.repositories;

import com.shorten.api.model.CountByDay;
import com.shorten.api.model.UrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<UrlEntity, Long> {

    /**
     * Find a Url entity by it's shortUrl mapping
     * @param shortUrl
     * @return
     */
    Optional<UrlEntity> findFirstByShortUrl(String shortUrl);

//    /**
//     * Find a Url entity by it's shortUrl mapping
//     * @param shortUrl
//     * @return
//     */
//    List<UrlEntity> findByShortUrl(String shortUrl);

    /**
     * Find a Url entity by it's longUrl mapping
     * @param longUrl
     * @return
     */
    List<UrlEntity> findByLongUrl(String longUrl);

    /**
     * Find all the Url entities added within a given date interval.
     * @param fromDate
     * @param toDate
     * @return
     */
    List<UrlEntity> findAllByDateAddedBetween(LocalDate fromDate, LocalDate toDate);

    /**
     * Find the count of the URL entities added per day within a given date interval.
     *
     * @param fromDate
     * @param toDate
     * @return
     */
    @Query(value = "SELECT count(dateadded) as count, dateadded as dateadded from url_store WHERE dateadded >= ? and dateadded <= ? group by dateadded",
            nativeQuery = true)
    List<CountByDay> getAddedCountByDay(LocalDate fromDate, LocalDate toDate);

}