package com.example.pricing.prices.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.example.pricing.prices.application.commands.GetPriceCommand;
import com.example.pricing.prices.application.exceptions.InvalidQueryException;
import com.example.pricing.prices.domain.Price;
import com.example.pricing.prices.domain.PriceMoney;
import com.example.pricing.prices.domain.PriceRepository;
import com.example.pricing.prices.domain.exceptions.PriceNotFoundException;

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
    void shouldRejectInvalidInputAndNotCallRepository() {
        // brandId <= 0
        assertThrows(InvalidQueryException.class,
                () -> useCase.handle(new GetPriceCommand(0L, 2L,
                        LocalDateTime.parse("2020-06-10T16:00:00"))));
        verifyNoInteractions(repository);

        // productId <= 0
        assertThrows(InvalidQueryException.class,
                () -> useCase.handle(new GetPriceCommand(1L, 0L,
                        LocalDateTime.parse("2020-06-10T16:00:00"))));
        verifyNoInteractions(repository);

        // date null
        assertThrows(InvalidQueryException.class,
                () -> useCase.handle(new GetPriceCommand(1L, 2L, null)));
        verifyNoInteractions(repository);
    }

    @Test
    void shouldReturnPriceWhenExists() {
        // Arrange
        long brandId = 1L;
        long productId = 2L;
        LocalDateTime date = LocalDateTime.parse("2020-06-14T16:00:00");
        PriceMoney money = PriceMoney.of(100L, "EUR");
        Price expected = Price.of(1L, brandId, date.minusHours(1), date.plusHours(1), 1L, 1L, productId, money);

        when(repository.findByBrandAndProductAndDate(brandId, productId, date)).thenReturn(List.of(expected));

        // Act
        Price result = useCase.handle(new GetPriceCommand(brandId, productId, date));

        // Assert
        assertEquals(expected, result);
        verify(repository).findByBrandAndProductAndDate(brandId, productId, date);
    }

    @Test
    void shouldSelectHighestPriorityFromUnsortedCandidates() {
        // Arrange
        long brandId = 1L;
        long productId = 2L;

        PriceMoney baseMoney = PriceMoney.of(100L, "EUR");
        Price base = Price.of(
                10L, brandId,
                LocalDateTime.parse("2020-06-10T00:00:00"),
                LocalDateTime.parse("2020-06-10T23:59:59"),
                1L, 0L, productId, baseMoney);

        PriceMoney promoMoney = PriceMoney.of(80L, "EUR");
        Price promo = Price.of(
                11L, brandId,
                LocalDateTime.parse("2020-06-10T15:00:00"),
                LocalDateTime.parse("2020-06-10T18:30:00"),
                2L, 1L, productId, promoMoney);

        LocalDateTime queryDate = LocalDateTime.parse("2020-06-10T16:00:00");
        when(repository.findByBrandAndProductAndDate(brandId, productId, queryDate))
                .thenReturn(List.of(base, promo));

        // Act
        Price result = useCase.handle(new GetPriceCommand(brandId, productId, queryDate));

        // Assert
        assertEquals(promo, result);
        verify(repository).findByBrandAndProductAndDate(brandId, productId, queryDate);
    }

    @Test
    void shouldThrowWhenPriceNotFound() {
        // Arrange
        long brandId = 1L;
        long productId = 2L;
        LocalDateTime date = LocalDateTime.parse("2020-06-10T00:00:00");

        when(repository.findByBrandAndProductAndDate(brandId, productId, date)).thenReturn(List.of());

        // Act & Assert
        assertThrows(PriceNotFoundException.class,
                () -> useCase.handle(new GetPriceCommand(brandId, productId, date)));
        verify(repository).findByBrandAndProductAndDate(brandId, productId, date);
    }
}
