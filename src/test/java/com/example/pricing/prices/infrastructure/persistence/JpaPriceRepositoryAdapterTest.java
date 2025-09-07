package com.example.pricing.prices.infrastructure.persistence;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.pricing.prices.domain.Price;

@DataJpaTest
class JpaPriceRepositoryAdapterTest {
    @Autowired
    JpaPriceRepositoryAdapter repository;

    @Test
    void shouldReturnPriceForFirstRange() {
        // Arrange
        LocalDateTime date = LocalDateTime.parse("2020-06-14T10:00:00");

        // Act
        List<Price> prices = repository.findByBrandAndProductAndDate(1L, 35455L, date);

        // Assert
        assertThat(prices).anyMatch(p -> p.getMoney().getPrice().compareTo(new java.math.BigDecimal("35.50")) == 0);
    }

    @Test
    void shouldReturnPromoPriceAt2020_06_14_16_00() {
        // Arrange
        LocalDateTime date = LocalDateTime.parse("2020-06-14T16:00:00");

        // Act
        List<Price> prices = repository.findByBrandAndProductAndDate(1L, 35455L, date);

        // Assert
        assertThat(prices)
                .as("At 2020-06-14 16:00 both base and promo ranges overlap; promo has higher priority")
                .anyMatch(p -> p.getPriceList() == 2 &&
                        p.getMoney().getPrice().compareTo(new java.math.BigDecimal("25.45")) == 0);
    }
}
