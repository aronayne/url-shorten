package com.shorten.api.integration;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
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

/**
 * These tests use the seeded DB data in resousrrces dir
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {StatsIntegrationTests.Initializer.class})
public class StatsIntegrationTests {

    @ClassRule
    public static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres")
            .withDatabaseName("shorten-db")
            .withUsername("my_user")
            .withPassword("my_password");
    @Value("http://localhost:${local.server.port}")
    String baseUrl;

    /**
     * Test the number of items added per day. This uses the seeded DB data.
     */
    @Test
    public void testItemsAddedPerDayService() {

        final String fromDate = "2020-08-27";
        final String toDate = "2020-08-30";

        given().get(baseUrl + "/stats/addedCountByDay/" + fromDate + "/" + toDate)
                .then()
                .statusCode(200)
                .body("size()", is(4))
                .body("[0].dateAdded", equalTo("2020-08-29T23:00:00.000+00:00"))
                .body("[0].count", equalTo(2))
                .body("[1].dateAdded", equalTo("2020-08-28T23:00:00.000+00:00"))
                .body("[1].count", equalTo(2))
                .body("[2].dateAdded", equalTo("2020-08-27T23:00:00.000+00:00"))
                .body("[2].count", equalTo(1))
                .body("[3].dateAdded", equalTo("2020-08-26T23:00:00.000+00:00"))
                .body("[3].count", equalTo(1));

    }

    /**
     * Test the top level API endpoint
     */
    @Test
    public void testBaseUrl() {

        final String dateFrom = "2020-08-27";
        final String dateTo = "2020-08-30";

        given().get(baseUrl + "/stats/" + dateFrom + "/" + dateTo)
                .then()
                .statusCode(200)
                .body("size()", is(2))
                .body("meanPerDay", equalTo(1.5f))
                .body("stdPerDay", equalTo(0.5f));

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
