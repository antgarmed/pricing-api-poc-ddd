package com.example.prices.prices.application;

import com.example.prices.prices.application.commands.GetPriceCommand;
import com.example.prices.prices.domain.Price;
import com.example.prices.prices.domain.PriceNotFoundException;
import com.example.prices.prices.domain.PriceRepository;
import com.example.prices.shared.application.UseCase;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class GetPriceUseCase {
    private final PriceRepository repository;

    public Price handle(GetPriceCommand command) {
        return repository.findByBrandAndProductAndDate(command.brandId(), command.productId(), command.date())
                .orElseThrow(() -> new PriceNotFoundException("Price not found"));
    }
}
