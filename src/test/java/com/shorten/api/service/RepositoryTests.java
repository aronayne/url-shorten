package com.shorten.api.service;

import com.shorten.api.model.URLShorten;
import com.shorten.api.model.UrlEntity;
import com.shorten.api.repositories.UrlRepository;
import com.shorten.api.system.Constants;
import com.shorten.api.system.Utilities;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {RepositoryTests.Initializer.class})
public class RepositoryTests {

    @ClassRule
    public static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres")
            .withDatabaseName("shorten-db")
            .withUsername("my_user")
            .withPassword("my_password");

    @Value("http://localhost:${local.server.port}")
    String baseUrl;

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private URLShorten urlCondenser;

    private static Date between(Date startInclusive, Date endExclusive) {
        long startMillis = startInclusive.getTime();
        long endMillis = endExclusive.getTime();
        long randomMillisSinceEpoch = ThreadLocalRandom.current().nextLong(startMillis, endMillis);

        return new Date(randomMillisSinceEpoch);
    }


    @Test
    public void testInsert() {

        final String shortUrl = "test123";
        urlRepository.save(new UrlEntity(new Date(), "https://www.amazon.com/Kindle-Wireless-Reading-Display-Globally/dp/B003FSUDM4/ref=amb_link_353259562_2?pf_rd_m=ATVPDKIKX0DER&pf_rd_s=center-10&pf_rd_r=11EYKTN682A79T370AM3&pf_rd_t=201&pf_rd_p=1270985982&pf_rd_i=B002Y27P3M", shortUrl));

        UrlEntity urlEntity = urlRepository.findFirstByShortUrl(shortUrl).get();

        Assertions.assertThat(urlEntity.getShortUrl().length()).isEqualTo(Constants.SHORT_URL_LENGTH);

        urlRepository.flush();
    }

    @Test
    public void testLargeInsert() {

        Date fromDate = Utilities.getDate("2020-08-31").get();
        Date toDate = Utilities.getDate("2020-10-31").get();

        final int entriesToAdd = 1000;

        List<Long> idList = new ArrayList<Long>();
        for (int i = 0; i < entriesToAdd; i++) {
            String randomUrl = RandomStringUtils.randomAlphabetic(47);
            String shortUrl = urlCondenser.shortenURL(randomUrl);
            UrlEntity u = new UrlEntity(between(fromDate, toDate), randomUrl, shortUrl);
            idList.add(urlRepository.save(u).getId());
        }

        List<UrlEntity> savedUrlEntity = urlRepository.findAllByDateAddedBetween(fromDate, toDate);
        List<UrlEntity> listOutput = savedUrlEntity.stream().filter(e -> idList.contains(e.getId())).collect(Collectors.toList());

        Assertions.assertThat(listOutput.size()).isEqualTo(entriesToAdd);
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
