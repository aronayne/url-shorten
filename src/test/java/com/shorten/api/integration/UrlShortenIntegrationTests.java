package com.shorten.api.integration;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.assertj.core.api.Assertions;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {UrlShortenIntegrationTests.Initializer.class})
public class UrlShortenIntegrationTests {

    @ClassRule
    public static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres")
            .withDatabaseName("shorten-db")
            .withUsername("my_user")
            .withPassword("my_password");

    @Value("http://localhost:${local.server.port}")
    String baseUrl;

    @Test
    public void testInvalidToDate() {

        given().get(baseUrl + "/shorten/created/2020-05-30/2020-12-33")
                .then()
                .statusCode(500);
    }


    /**
     * Test can access a DB seeded entry.
     */
    @Test
    public void testGetById() {

        given().get(baseUrl + "/shorten/2")
                .then()
                .statusCode(200)
                .body("size()", is(4))
                .body("id", equalTo(2))
                .body("longUrl", equalTo("https://www.baeldung.com/configuration-properties-in-spring-boot-2"))
                .body("shortUrl", equalTo("short13"))
                .body("dateAdded", equalTo("2020-08-30"));
    }

    /**
     * If the long URL has not been added then a 400 status code is returned
     */
    @Test
    public void testLongUrlOmmited() {
        Response response = given().put(baseUrl + "/shorten/");
        System.out.println("response" + response.asString());
        JsonPath jsonPath = new JsonPath(response.asString());
        Assertions.assertThat(jsonPath.getInt("status")).isEqualTo(400);
    }


    /**
     * Test a short URL redirects a client to the correct long URL mapping
     * @throws Exception
     */
    @Test
    public void testShortUrlRedirect() throws Exception {

        HttpUriRequest request = new HttpPut(baseUrl + "/shorten/?longUrl=https://www.google.com/");
        HttpResponse putResponse = HttpClientBuilder.create().build().execute(request);
        String shortUrl = EntityUtils.toString(putResponse.getEntity());
        Connection.Response response = Jsoup.connect(baseUrl + "/shorten/redirect/" + shortUrl).execute();
        Document document = Jsoup.connect(String.valueOf(response.url())).get();
        String title = document.title();

        Assertions.assertThat(title).isEqualTo("Google");

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
