package com.shorten.api.service;

import com.shorten.api.exception.LongUrlLengthExceededException;
import com.shorten.api.exception.LongUrlNotFoundException;
import com.shorten.api.model.StatsSummary;
import com.shorten.api.services.StatsService;
import com.shorten.api.services.UrlShortenService;
import com.shorten.api.system.Utilities;
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

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

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

    @Autowired
    private StatsService statsService;

    private static Date between(Date startInclusive, Date endExclusive) {
        long startMillis = startInclusive.getTime();
        long endMillis = endExclusive.getTime();
        long randomMillisSinceEpoch = ThreadLocalRandom.current().nextLong(startMillis, endMillis);

        return new Date(randomMillisSinceEpoch);
    }

    @Test
    public void testShortUrlNotFoundException() {
        exceptionRule.expect(LongUrlNotFoundException.class);
        exceptionRule.expectMessage("URL not found for the given ID");
        urlShortenService.findById((long) 99999);
    }

    @Test
    public void testLongUrlLengthExceeded() {

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 2000; i++) {
            sb.append("s");
        }
        exceptionRule.expect(LongUrlLengthExceededException.class);
        exceptionRule.expectMessage("URL with length 2000 exceeds the max length of 2000 characters");
        urlShortenService.saveUrlEntity(sb.toString() , new Date());
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
