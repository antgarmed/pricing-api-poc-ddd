package com.example.prices.prices.application;

import java.util.Comparator;
import java.util.List;

import com.example.prices.prices.application.commands.GetPriceCommand;
import com.example.prices.prices.application.exceptions.InvalidQueryException;
import com.example.prices.prices.domain.Price;
import com.example.prices.prices.domain.PriceRepository;
import com.example.prices.prices.domain.exceptions.PriceNotFoundException;
import com.example.prices.shared.application.UseCase;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class GetPriceUseCase {
    private final PriceRepository repository;

    public Price handle(GetPriceCommand command) {
        validate(command);

        List<Price> candidates = repository.findByBrandAndProductAndDate(command.brandId(), command.productId(),
                command.date());

        return candidates.stream()
                .max(Comparator
                        .comparingLong(Price::getPriority).reversed()
                        .thenComparing(Price::getStartDate, Comparator.reverseOrder())
                        .thenComparingLong(Price::getPriceList).reversed())
                .orElseThrow(() -> new PriceNotFoundException("Price not found"));
    }

    private void validate(GetPriceCommand command) {
        if (command == null || command.date() == null || command.brandId() <= 0 || command.productId() <= 0) {
            throw new InvalidQueryException("brandId > 0, productId > 0 and date != null are required");
        }
    }
}
