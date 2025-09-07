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
                // start/end shifted by timezone like your first test
                .body("startDate", equalTo("2020-06-14T02:00:00"))
                .body("endDate", equalTo("2021-01-01T00:59:59"))
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
                // promo window 15:00–18:30 local → +2h shift in June
                .body("startDate", equalTo("2020-06-14T17:00:00"))
                .body("endDate", equalTo("2020-06-14T20:30:00"))
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
                // base window again
                .body("startDate", equalTo("2020-06-14T02:00:00"))
                .body("endDate", equalTo("2021-01-01T00:59:59"))
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
                // 00:00–11:00 local on June 15th → +2h shift
                .body("startDate", equalTo("2020-06-15T02:00:00"))
                .body("endDate", equalTo("2020-06-15T13:00:00"))
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
                // 15/06 16:00–12/31 23:59:59 local → shifts +2h (start) and +1h (end in winter)
                .body("startDate", equalTo("2020-06-15T18:00:00"))
                .body("endDate", equalTo("2021-01-01T00:59:59"))
                .body("money.currency", equalTo("EUR"))
                .body("money.price", closeTo(new BigDecimal("38.95"), new BigDecimal("0.0001")));
    }
}
