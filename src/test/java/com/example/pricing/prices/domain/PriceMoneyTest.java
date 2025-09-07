package com.example.pricing.prices.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

public class PriceMoneyTest {
    @Test
    void shouldCreatePriceMoney() {
        // Arrange
        BigDecimal amount = BigDecimal.valueOf(100);
        String currency = "EUR";

        // Act
        PriceMoney priceMoney = PriceMoney.of(amount, currency);

        // Assert
        assertNotNull(priceMoney);
        assertEquals(amount, priceMoney.getPrice());
        assertEquals(currency, priceMoney.getCurrency());
    }

    @Test
    void shouldCreatePriceMoneyWithLongAmount() {
        // Arrange
        long amount = 100L;
        String currency = "EUR";

        // Act
        PriceMoney priceMoney = PriceMoney.of(amount, currency);

        // Assert
        assertNotNull(priceMoney);
        assertEquals(BigDecimal.valueOf(amount), priceMoney.getPrice());
        assertEquals(currency, priceMoney.getCurrency());
    }

    @Test
    void shouldThrowExceptionForNegativeAmount() {
        // Arrange
        BigDecimal amount = BigDecimal.valueOf(-100);
        String currency = "EUR";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> PriceMoney.of(amount, currency));
    }
}
