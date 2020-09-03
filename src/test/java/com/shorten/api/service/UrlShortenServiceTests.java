package com.shorten.api.service;

import com.shorten.api.exception.InvalidDateException;
import com.shorten.api.exception.LongUrlLengthExceededException;
import com.shorten.api.exception.UrlEntityNotFoundException;
import com.shorten.api.model.UrlEntity;
import com.shorten.api.services.UrlShortenService;
import org.assertj.core.api.Assertions;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.LocalDate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {UrlShortenServiceTests.Initializer.class})
public class UrlShortenServiceTests {

    @ClassRule
    public static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres")
            .withDatabaseName("shorten-db")
            .withUsername("my_user")
            .withPassword("my_password");
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Autowired
    private UrlShortenService urlShortenService;

    /**
     * Test InvalidDateException is thrown when an invalid from date is used to search for URL entities.
     */
    @Test
    public void testInvalidFromDateException() {
        exceptionRule.expect(InvalidDateException.class);
        exceptionRule.expectMessage("The date is invalid");
        urlShortenService.findAllByDateAddedBetween("2020-05-33", "2020-12-05");
    }

    /**
     * Test InvalidDateException is thrown when an invalid to date is used to search for URL entities.
     */
    @Test
    public void testInvalidToDateException() {
        exceptionRule.expect(InvalidDateException.class);
        exceptionRule.expectMessage("The date is invalid");
        urlShortenService.findAllByDateAddedBetween("2020-05-30", "2020-12-323");
    }

    /**
     * Test InvalidDateException is thrown when an invalid from date and to date is used to search for URL entities.
     */
    @Test
    public void testInvalidFromAndToDateException() {
        exceptionRule.expect(InvalidDateException.class);
        exceptionRule.expectMessage("The date is invalid");
        urlShortenService.findAllByDateAddedBetween("2020-05-33", "2020-12-323");
    }

    /**
     * Test LongUrlNotFoundException is thrown when URL is not found for a short URL
     */
    @Test
    public void testShortUrlNotFoundException() {
        exceptionRule.expect(UrlEntityNotFoundException.class);
        exceptionRule.expectMessage("URL not found for the given ID");
        urlShortenService.findById((long) 99999);
    }

    /**
     * Test LongUrlLengthExceededException is thrown when the length of long URL exceeds 2000 characters
     */
    @Test
    public void testLongUrlLengthExceeded() {

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 2000; i++) {
            sb.append("s");
        }
        exceptionRule.expect(LongUrlLengthExceededException.class);
        exceptionRule.expectMessage("URL with length 2000 exceeds the max length of 2000 characters");
        urlShortenService.saveUrlEntity(sb.toString(), LocalDate.now());
    }

    /**
     * Test the short URL is reused when multiple long URL's are shortened.
     */
    @Test
    public void testShortUrlReused() {
        UrlEntity urlEntity1 = urlShortenService.saveUrlEntity("www.testurl.com", LocalDate.now());
        UrlEntity urlEntity2 = urlShortenService.saveUrlEntity("www.testurl.com", LocalDate.now());

        Assertions.assertThat(urlEntity1.getShortUrl()).isEqualTo(urlEntity2.getShortUrl());

    }

    /**
     * Test can delete the URL entity using the id
     */
    @Test
    public void testDeleteById() {
        UrlEntity urlEntity = urlShortenService.saveUrlEntity("www.testurl1234.com", LocalDate.now());
        UrlEntity savedUrlEntity = urlShortenService.findById(urlEntity.getId()).get();
        Assertions.assertThat(savedUrlEntity.getLongUrl()).isEqualTo("www.testurl1234.com");
        urlShortenService.deleteById(urlEntity.getId());
        exceptionRule.expect(UrlEntityNotFoundException.class);
        exceptionRule.expectMessage("URL not found for the given ID");
        urlShortenService.findById(urlEntity.getId());
    }

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgres.getJdbcUrl(),
                    "spring.datasource.username=" + postgres.getUsername(),
                    "spring.datasource.password=" + postgres.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

}
