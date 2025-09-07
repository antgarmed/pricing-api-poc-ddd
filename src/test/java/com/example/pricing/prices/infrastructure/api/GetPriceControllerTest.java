package com.example.pricing.prices.infrastructure.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.number.BigDecimalCloseTo.closeTo;

import io.restassured.http.ContentType;
import io.restassured.path.json.config.JsonPathConfig.NumberReturnType;
import io.restassured.RestAssured;
import io.restassured.config.JsonConfig;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetPriceControllerTest {
    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.config = RestAssured.config()
            .jsonConfig(JsonConfig.jsonConfig()
                .numberReturnType(NumberReturnType.BIG_DECIMAL));
    }

    @Test
    void shouldReturnApplicablePriceFor2020_06_14_10_00() {
        given()
            .port(port)
            .basePath("/prices")
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .queryParam("date", "2020-06-14T10:00:00")
            .queryParam("productId", 35455)
            .queryParam("brandId", 1)
        .when()
            .get()
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("productId", equalTo(35455))
            .body("brandId", equalTo(1))
            .body("priceList", equalTo(1))
            .body("startDate", equalTo("2020-06-14T00:00:00"))
            .body("endDate", equalTo("2020-12-31T23:59:59"))
            .body("money.currency", equalTo("EUR"))
            .body("money.price", closeTo(new BigDecimal("35.50"), new BigDecimal("0.0001")));
    }

    @Test
    void shouldReturnApplicablePriceFor2020_06_14_16_00() {
        given()
            .port(port)
            .basePath("/prices")
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .queryParam("date", "2020-06-14T16:00:00")
            .queryParam("productId", 35455)
            .queryParam("brandId", 1)
        .when()
            .get()
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("productId", equalTo(35455))
            .body("brandId", equalTo(1))
            .body("priceList", equalTo(2))
            .body("startDate", equalTo("2020-06-14T15:00:00"))
            .body("endDate", equalTo("2020-06-14T18:30:00"))
            .body("money.currency", equalTo("EUR"))
            .body("money.price", closeTo(new BigDecimal("25.45"), new BigDecimal("0.0001")));
    }

    @Test
    void shouldReturnApplicablePriceFor2020_06_14_21_00() {
        given()
            .port(port)
            .basePath("/prices")
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .queryParam("date", "2020-06-14T21:00:00")
            .queryParam("productId", 35455)
            .queryParam("brandId", 1)
        .when()
            .get()
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("productId", equalTo(35455))
            .body("brandId", equalTo(1))
            .body("priceList", equalTo(1))
            .body("startDate", equalTo("2020-06-14T00:00:00"))
            .body("endDate", equalTo("2020-12-31T23:59:59"))
            .body("money.currency", equalTo("EUR"))
            .body("money.price", closeTo(new BigDecimal("35.50"), new BigDecimal("0.0001")));
    }

    @Test
    void shouldReturnApplicablePriceFor2020_06_15_10_00() {
        given()
            .port(port)
            .basePath("/prices")
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .queryParam("date", "2020-06-15T10:00:00")
            .queryParam("productId", 35455)
            .queryParam("brandId", 1)
        .when()
            .get()
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("productId", equalTo(35455))
            .body("brandId", equalTo(1))
            .body("priceList", equalTo(3))
            .body("startDate", equalTo("2020-06-15T00:00:00"))
            .body("endDate", equalTo("2020-06-15T11:00:00"))
            .body("money.currency", equalTo("EUR"))
            .body("money.price", closeTo(new BigDecimal("30.50"), new BigDecimal("0.0001")));
    }

    @Test
    void shouldReturnApplicablePriceFor2020_06_16_21_00() {
        given()
            .port(port)
            .basePath("/prices")
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .queryParam("date", "2020-06-16T21:00:00")
            .queryParam("productId", 35455)
            .queryParam("brandId", 1)
        .when()
            .get()
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("productId", equalTo(35455))
            .body("brandId", equalTo(1))
            .body("priceList", equalTo(4))
            .body("startDate", equalTo("2020-06-15T16:00:00"))
            .body("endDate", equalTo("2020-12-31T23:59:59"))
            .body("money.currency", equalTo("EUR"))
            .body("money.price", closeTo(new BigDecimal("38.95"), new BigDecimal("0.0001")));
    }

    @Test
    void shouldReturn404WhenNoPriceFound() {
        given()
            .port(port)
            .basePath("/prices")
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .queryParam("date", "2019-01-01T10:00:00")
            .queryParam("productId", 35455)
            .queryParam("brandId", 1)
        .when()
            .get()
        .then()
            .statusCode(404)
            .contentType(ContentType.JSON);
    }

    @Test
    void shouldReturn400WhenBrandIdIsInvalid() {
        given()
            .port(port)
            .basePath("/prices")
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .queryParam("date", "2020-06-14T10:00:00")
            .queryParam("productId", 35455)
            .queryParam("brandId", 0)
        .when()
            .get()
        .then()
            .statusCode(400)
            .contentType(ContentType.JSON);
    }
}
