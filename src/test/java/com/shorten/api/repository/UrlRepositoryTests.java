package com.shorten.api.repository;

import com.shorten.api.model.URLShorten;
import com.shorten.api.model.UrlEntity;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Test URL repository methods.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {UrlRepositoryTests.Initializer.class})
public class UrlRepositoryTests {

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

    @Test
    public void testInsert() {

        final String shortUrl = "test123";
        urlRepository.save(new UrlEntity(LocalDate.now(), "https://www.amazon.com/Kindle-Wireless-Reading-Display-Globally/dp/B003FSUDM4/ref=amb_link_353259562_2?pf_rd_m=ATVPDKIKX0DER&pf_rd_s=center-10&pf_rd_r=11EYKTN682A79T370AM3&pf_rd_t=201&pf_rd_p=1270985982&pf_rd_i=B002Y27P3M", shortUrl));

        UrlEntity urlEntity = urlRepository.findFirstByShortUrl(shortUrl).get();

        Assertions.assertThat(urlEntity.getShortUrl().length()).isEqualTo(Constants.SHORT_URL_LENGTH);
    }

    @Test
    public void testLargeInsert() {

        LocalDate fromDate = Utilities.getDate("2020-08-31");
        LocalDate toDate = Utilities.getDate("2020-10-31");

        final int entriesToAdd = 1000;

        List<Long> idList = new ArrayList<Long>();
        for (int i = 0; i < entriesToAdd; i++) {
            String randomUrl = RandomStringUtils.randomAlphabetic(47);
            String shortUrl = urlCondenser.shortenURL(randomUrl);
            UrlEntity u = new UrlEntity(fromDate, randomUrl, shortUrl);
            idList.add(urlRepository.save(u).getId());
        }

        List<UrlEntity> savedUrlEntity = urlRepository.findAllByDateAddedBetween(fromDate, toDate);
        List<UrlEntity> listOutput = savedUrlEntity.stream().filter(e -> idList.contains(e.getId())).collect(Collectors.toList());

        Assertions.assertThat(listOutput.size()).isEqualTo(entriesToAdd);
    }

    /**
     * Initialize the test application context
     */
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
