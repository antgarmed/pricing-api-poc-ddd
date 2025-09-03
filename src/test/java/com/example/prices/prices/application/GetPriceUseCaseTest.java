package com.example.prices.prices.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.example.prices.prices.application.commands.GetPriceCommand;
import com.example.prices.prices.domain.Price;
import com.example.prices.prices.domain.PriceMoney;
import com.example.prices.prices.domain.PriceNotFoundException;
import com.example.prices.prices.domain.PriceRepository;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

public class GetPriceUseCaseTest {
    private PriceRepository repository;
    private GetPriceUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(PriceRepository.class);
        useCase = new GetPriceUseCase(repository);
    }

    @Test
    void shouldReturnPriceWhenExists() {
        // Arrange
        long brandId = 1L;
        long productId = 2L;
        LocalDateTime date = LocalDateTime.parse("2020-06-14T16:00:00");
        PriceMoney money = PriceMoney.of(100L, "EUR");
        Price expected = Price.of(1L, brandId, date.minusHours(1), date.plusHours(1), 1L, 1L, productId, money);

        when(repository.findByBrandAndProductAndDate(brandId, productId, date)).thenReturn(Optional.of(expected));

        // Act
        Price result = useCase.handle(new GetPriceCommand(brandId, productId, date));

        // Assert
        assertEquals(expected, result);
        verify(repository).findByBrandAndProductAndDate(brandId, productId, date);
    }

    @Test
    void shouldThrowWhenPriceNotFound() {
        // Arrange
        long brandId = 1L;
        long productId = 2L;
        LocalDateTime date = LocalDateTime.now();

        when(repository.findByBrandAndProductAndDate(brandId, productId, date)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PriceNotFoundException.class,
                () -> useCase.handle(new GetPriceCommand(brandId, productId, date)));
        verify(repository).findByBrandAndProductAndDate(brandId, productId, date);
    }
}
