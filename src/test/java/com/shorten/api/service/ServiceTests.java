package com.shorten.api.service;

import com.shorten.api.exception.UrlLengthExceededException;
import com.shorten.api.exception.UrlNotFoundException;
import com.shorten.api.services.UrlService;
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
@ContextConfiguration(initializers = {ServiceTests.Initializer.class})
public class ServiceTests {

    @ClassRule
    public static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres")
            .withDatabaseName("shorten-db")
            .withUsername("my_user")
            .withPassword("my_password");
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Autowired
    private UrlService urlService;

    private static Date between(Date startInclusive, Date endExclusive) {
        long startMillis = startInclusive.getTime();
        long endMillis = endExclusive.getTime();
        long randomMillisSinceEpoch = ThreadLocalRandom.current().nextLong(startMillis, endMillis);

        return new Date(randomMillisSinceEpoch);
    }

    @Test
    public void testShortUrlNotFoundException() {
        exceptionRule.expect(UrlNotFoundException.class);
        exceptionRule.expectMessage("URL not found for the given ID");
        urlService.findById((long) 99999);
    }

    @Test
    public void testLongUrlLengthExceeded() {

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 2000; i++) {
            sb.append("s");
        }
        exceptionRule.expect(UrlLengthExceededException.class);
        exceptionRule.expectMessage("URL with length 2000 exceeds the max length of 2000 characters");
        urlService.saveUrlEntity(sb.toString() , new Date());
    }

    @Test
    public void testFindByShortUrl() {
        urlService.findByShortUrl("short16");
    }

//
//    given().get(baseUrl+"/urls/2")
//                .then()
//                .statusCode(200)
//                .body("size()", is(4))
//            .body("id", equalTo(2))
//            .body("longUrl", equalTo("https://www.baeldung.com/configuration-properties-in-spring-boot-2"))
//            .body("shortUrl", equalTo("short13"))
//            .body("dateAdded", equalTo("2020-08-29T23:00:00.000+00:00"));

//    @Test
//    public void testInsert() {
//
//        final String shortUrl = "test123";
//        urlRepository.save(new Url(new Date(), "https://www.amazon.com/Kindle-Wireless-Reading-Display-Globally/dp/B003FSUDM4/ref=amb_link_353259562_2?pf_rd_m=ATVPDKIKX0DER&pf_rd_s=center-10&pf_rd_r=11EYKTN682A79T370AM3&pf_rd_t=201&pf_rd_p=1270985982&pf_rd_i=B002Y27P3M", shortUrl));
//
//        List<Url> savedUrl = urlRepository.findByShortUrlIs(shortUrl);
//        Url url = savedUrl.get(0);
//
//        Assertions.assertThat(savedUrl.size()).isEqualTo(1);
//        Assertions.assertThat(url.getShortUrl().length()).isEqualTo(Constants.SHORT_URL_LENGTH);
//
//
//        urlRepository.flush();

//        insertUrls();
//
//        System.out.println("urlRepository.findAll().size())"+urlRepository.findAll().size());
//
//        Assertions.assertThat(urlRepository.findAll().size()).isEqualTo(21);
//    }
//
//
//    @Test
//    public void testLargeInsert()  {
//
//        Date fromDate = Utilities.getDate("2020-08-31").get();
//        Date toDate =  Utilities.getDate("2020-10-31").get();
//
//        final int entriesToAdd = 1000;
//
//        List<Long> idList = new ArrayList<Long>();
//        for (int i = 0; i < entriesToAdd; i++) {
//            String randomUrl = RandomStringUtils.randomAlphabetic(47);
//            String shortUrl = urlCondenser.shortenURL(randomUrl);
//            Url u = new Url(between(fromDate, toDate), randomUrl, shortUrl);
//            idList.add(urlRepository.save(u).getId());
//        }
//
//        List<Url> savedUrl = urlRepository.findAllByDateAddedBetween(fromDate, toDate);
//        List<Url> listOutput = savedUrl.stream().filter(e -> idList.contains(e.getId())).collect(Collectors.toList());
//
//        Assertions.assertThat(listOutput.size()).isEqualTo(entriesToAdd);
//    }


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
