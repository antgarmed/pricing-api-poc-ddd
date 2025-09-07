package com.example.pricing.prices.infrastructure.persistence;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.pricing.prices.domain.Price;
import com.example.pricing.prices.domain.PriceRepository;

@DataJpaTest
class JpaPriceRepositoryAdapterTest {
    @Autowired
    PriceRepository repository;

    @Test
    void shouldReturnPriceForFirstRange() {
        // Arrange
        LocalDateTime date = LocalDateTime.parse("2020-06-14T10:00:00");

        // Act
        List<Price> prices = repository.findByBrandAndProductAndDate(1L, 35455L, date);

        // Assert
        assertThat(prices).anyMatch(p -> p.getMoney().getPrice().compareTo(new java.math.BigDecimal("35.50")) == 0);
    }
}
