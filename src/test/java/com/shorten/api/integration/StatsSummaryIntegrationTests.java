package com.shorten.api.integration;

import com.shorten.api.services.StatsService;
import com.shorten.api.services.UrlShortenService;
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

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {StatsSummaryIntegrationTests.Initializer.class})
public class StatsSummaryIntegrationTests {

    @ClassRule
    public static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres")
            .withDatabaseName("shorten-db")
            .withUsername("my_user")
            .withPassword("my_password");
    @Value("http://localhost:${local.server.port}")
    String baseUrl;
    @Autowired
    private UrlShortenService urlShortenService;
    @Autowired
    private StatsService statsService;

    @Test
    public void testItemsAddedPerDayService() {

        final String dateFrom = "2020-08-27";
        final String dateTo = "2020-08-30";

        given().get(baseUrl + "/stats/addedCountByDay/" + dateFrom + "/" + dateTo)
                .then()
                .statusCode(200)
                .body("size()", is(4))
                .body("[0].dateAdded", equalTo("2020-08-26T23:00:00.000+00:00"))
                .body("[0].count", equalTo(1));

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
