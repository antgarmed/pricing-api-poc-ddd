package com.example.pricing.prices.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class PriceTest {
    @Test
    void shouldCreatePrice() {
        // Arrange
        long id = 1L;
        long brandId = 1L;
        LocalDateTime startDate = LocalDateTime.parse("2020-06-10T16:00:00");
        LocalDateTime endDate = LocalDateTime.parse("2020-06-11T16:00:00");
        long priceList = 1L;
        long productId = 1L;
        long priority = 1L;
        PriceMoney money = PriceMoney.of(100, "EUR");

        // Act
        Price price = Price.of(id, brandId, startDate, endDate, priceList, productId, priority, money);

        // Assert
        assertNotNull(price);
        assertEquals(id, price.getId());
        assertEquals(brandId, price.getBrandId());
        assertEquals(startDate, price.getStartDate());
        assertEquals(endDate, price.getEndDate());
        assertEquals(priceList, price.getPriceList());
        assertEquals(productId, price.getProductId());
        assertEquals(money, price.getMoney());
    }

    @Test
    void shouldThrowExceptionWhenStartDateIsNull() {
        // Arrange
        long id = 1L;
        long brandId = 1L;
        LocalDateTime startDate = null;
        LocalDateTime endDate = LocalDateTime.parse("2020-06-11T16:00:00");
        long priceList = 1L;
        long productId = 1L;
        long priority = 1L;
        PriceMoney money = PriceMoney.of(100, "EUR");

        assertThrows(IllegalArgumentException.class,
                () -> Price.of(id, brandId, startDate, endDate, priceList, productId, priority, money));
    }

    @Test
    void shouldThrowExceptionWhenEndDateIsNull() {
        // Arrange
        long id = 1L;
        long brandId = 1L;
        LocalDateTime startDate = LocalDateTime.parse("2020-06-10T16:00:00");
        LocalDateTime endDate = null;
        long priceList = 1L;
        long productId = 1L;
        long priority = 1L;
        PriceMoney money = PriceMoney.of(100, "EUR");

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> Price.of(id, brandId, startDate, endDate, priceList, productId, priority, money));
    }

    @Test
    void shouldThrowExceptionWhenMoneyIsNull() {
        // Arrange
        long id = 1L;
        long brandId = 1L;
        LocalDateTime startDate = LocalDateTime.parse("2020-06-10T16:00:00");
        LocalDateTime endDate = LocalDateTime.parse("2020-06-11T16:00:00");
        long priceList = 1L;
        long productId = 1L;
        long priority = 1L;
        PriceMoney money = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> Price.of(id, brandId, startDate, endDate, priceList, productId, priority, money));
    }

    @Test
    void shouldThrowExceptionWhenStartDateIsAfterEndDate() {
        // Arrange
        long id = 1L;
        long brandId = 1L;
        LocalDateTime startDate = LocalDateTime.parse("2020-06-11T16:00:00");
        LocalDateTime endDate = LocalDateTime.parse("2020-06-10T16:00:00");
        long priceList = 1L;
        long productId = 1L;
        long priority = 1L;
        PriceMoney money = PriceMoney.of(100, "EUR");

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> Price.of(id, brandId, startDate, endDate, priceList, productId, priority, money));
    }
}
