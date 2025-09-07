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
        String date = "2020-06-14T10:00:00";

        given()
                .port(port)
                .basePath("/prices")
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .queryParam("date", date)
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
                .body("startDate", equalTo("2020-06-14T02:00:00"))
                .body("endDate", equalTo("2021-01-01T00:59:59"))
                .body("money.currency", equalTo("EUR"))
                // price as BigDecimal ≈ 35.50 (tolerant to scale differences)
                .body("money.price", closeTo(new BigDecimal("35.50"), new BigDecimal("0.0001")));
    }
}
