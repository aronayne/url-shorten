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
@ContextConfiguration(initializers = {StatsServiceTests.Initializer.class})
public class StatsServiceTests {

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
    public void testStatistics() {

        Date date1 = Utilities.getDate("2019-08-16").get();
        Date date2 = Utilities.getDate("2019-08-17").get();

        urlShortenService.saveUrlEntity("http://www.thisisalongurltest1.com", date1);
        urlShortenService.saveUrlEntity("http://www.thisisalongurltest2.com", date1);
        urlShortenService.saveUrlEntity("http://www.thisisalongurltest3.com", date1);
        urlShortenService.saveUrlEntity("http://www.thisisalongurltest4.com", date1);
        urlShortenService.saveUrlEntity("http://www.thisisalongurltest5.com", date2);
        urlShortenService.saveUrlEntity("http://www.thisisalongurltest6.com", date2);
        urlShortenService.saveUrlEntity("http://www.thisisalongurltest7.com", date2);

        StatsSummary stats = statsService.getStatsSummary(date1, date2);
        Assertions.assertThat(stats.getMeanPerDay()).isEqualTo(3.5);
        Assertions.assertThat(stats.getStdPerDay()).isEqualTo(0.5);

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
