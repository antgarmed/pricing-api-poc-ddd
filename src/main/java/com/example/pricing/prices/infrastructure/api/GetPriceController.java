package com.example.pricing.prices.infrastructure.api;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.pricing.prices.application.GetPriceUseCase;
import com.example.pricing.prices.application.commands.GetPriceCommand;
import com.example.pricing.prices.domain.Price;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/prices")
@Validated
@RequiredArgsConstructor
public class GetPriceController {
    private final GetPriceUseCase useCase;

    @GetMapping
    public Price handle(
            @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date,
            @RequestParam @Positive long productId,
            @RequestParam @Positive long brandId) {
        Price price = useCase.handle(new GetPriceCommand(brandId, productId, date));
        return price;
    }
}
