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
        long brandId = 1L;
        long productId = 2L;
        LocalDateTime date = LocalDateTime.now();
        PriceMoney money = PriceMoney.of(100L, "EUR");
        Price expected = Price.of(1L, brandId, date.minusHours(1), date.plusHours(1), 1L, 1L, productId, money);

        when(repository.findByBrandAndProductAndDate(brandId, productId, date)).thenReturn(Optional.of(expected));

        Price result = useCase.handle(new GetPriceCommand(brandId, productId, date));

        assertEquals(expected, result);
        verify(repository).findByBrandAndProductAndDate(brandId, productId, date);
    }

    @Test
    void shouldThrowWhenPriceNotFound() {
        long brandId = 1L;
        long productId = 2L;
        LocalDateTime date = LocalDateTime.now();

        when(repository.findByBrandAndProductAndDate(brandId, productId, date)).thenReturn(Optional.empty());

        assertThrows(PriceNotFoundException.class,
                () -> useCase.handle(new GetPriceCommand(brandId, productId, date)));
        verify(repository).findByBrandAndProductAndDate(brandId, productId, date);
    }
}
