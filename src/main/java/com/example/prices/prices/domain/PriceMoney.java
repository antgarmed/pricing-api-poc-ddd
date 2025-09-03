package com.example.prices.prices.domain;

import java.math.BigDecimal;

import lombok.Value;

@Value
public class PriceMoney {
    BigDecimal price;
    String currency;

    private PriceMoney(BigDecimal amount, String currency) {
        this.price = amount;
        this.currency = currency;
    }

    public static PriceMoney of(BigDecimal amount, String currency) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        if (currency == null || currency.trim().isEmpty()) {
            throw new IllegalArgumentException("Currency must not be blank");
        }

        return new PriceMoney(amount, currency);
    }

    public static PriceMoney of(long amount, String currency) {
        return of(BigDecimal.valueOf(amount), currency);
    }
}
