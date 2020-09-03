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
    //    @Temporal(TemporalType.TIMESTAMP)
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

    public LocalDate getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }

    //TODO store time also
    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public Long getId() {
        return id;
    }

}