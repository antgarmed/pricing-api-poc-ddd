package com.example.prices.prices.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class PriceTest {
    @Test
    void shouldCreatePrice() {
        // Arrange
        long id = 1L;
        long brandId = 1L;
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusDays(1);
        long priceList = 1L;
        long productId = 1L;
        PriceMoney money = PriceMoney.of(100, "EUR");

        // Act
        Price price = Price.of(id, brandId, startDate, endDate, priceList, productId, money);

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
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        long priceList = 1L;
        long productId = 1L;
        PriceMoney money = PriceMoney.of(100, "EUR");

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> Price.of(id, brandId, startDate, endDate, priceList, productId, money));
    }

    @Test
    void shouldThrowExceptionWhenEndDateIsNull() {
        // Arrange
        long id = 1L;
        long brandId = 1L;
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = null;
        long priceList = 1L;
        long productId = 1L;
        PriceMoney money = PriceMoney.of(100, "EUR");

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> Price.of(id, brandId, startDate, endDate, priceList, productId, money));
    }

    @Test
    void shouldThrowExceptionWhenMoneyIsNull() {
        // Arrange
        long id = 1L;
        long brandId = 1L;
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusDays(1);
        long priceList = 1L;
        long productId = 1L;
        PriceMoney money = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> Price.of(id, brandId, startDate, endDate, priceList, productId, money));
    }

    @Test
    void shouldThrowExceptionWhenStartDateIsAfterEndDate() {
        // Arrange
        long id = 1L;
        long brandId = 1L;
        LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        LocalDateTime endDate = LocalDateTime.now();
        long priceList = 1L;
        long productId = 1L;
        PriceMoney money = PriceMoney.of(100, "EUR");

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> Price.of(id, brandId, startDate, endDate, priceList, productId, money));
    }
}
