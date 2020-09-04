package com.shorten.api.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "URL_STORE")
public class UrlEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "longurl")
    private String longUrl;
    @Column(name = "shorturl")
    private String shortUrl;
    @Column(name = "dateadded")
    private LocalDate dateAdded;

    public UrlEntity() {
        super();
    }

    public UrlEntity(LocalDate dateAdded, String longUrl, String shortUrl) {
        super();
        this.dateAdded = dateAdded;
        this.longUrl = longUrl;
        this.shortUrl = shortUrl;
    }

    /**
     * Get the date added of the URL entity
     *
     * @return the date added of the URL entity
     */
    public LocalDate getDateAdded() {
        return dateAdded;
    }

    /**
     * Set the date added of the URL entity
     *
     * @param dateAdded
     */
    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }

    /**
     * Get the short URL of the URL entity
     *
     * @return
     */
    public String getShortUrl() {
        return shortUrl;
    }

    /**
     * Set the short URL of the URL entity
     *
     * @return
     */
    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    /**
     * Get the long URL of the URL entity
     *
     * @return
     */
    public String getLongUrl() {
        return longUrl;
    }

    /**
     * Set the long URL of the URL entity
     *
     * @param longUrl
     */
    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    /**
     * Get the DB id of the URL entity
     *
     * @return DB id of the URL entity
     */
    public Long getId() {
        return id;
    }

}