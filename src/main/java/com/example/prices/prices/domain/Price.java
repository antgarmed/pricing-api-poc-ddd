package com.example.prices.prices.domain;

import java.time.LocalDateTime;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
public class Price {
    @Getter
    private long id;
    @Getter
    private long brandId;
    @Getter
    private LocalDateTime startDate;
    @Getter
    private LocalDateTime endDate;
    @Getter
    private long priceList;
    @Getter
    private long productId;
    @Getter
    private long priority;
    @Getter
    private PriceMoney money;

    private Price(long id, long brandId, LocalDateTime startDate, LocalDateTime endDate, long priceList, long productId,
            PriceMoney money) {
        this.id = id;
        this.brandId = brandId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.priceList = priceList;
        this.productId = productId;
        this.money = money;
    }

    public static Price of(long id, long brandId, LocalDateTime startDate, LocalDateTime endDate, long priceList,
            long priority, long productId, PriceMoney money) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("StartDate and EndDate must not be null");
        }

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("StartDate must be before EndDate");
        }

        if (priceList <= 0) {
            throw new IllegalArgumentException("PriceList must be positive");
        }

        if (priority < 0) {
            throw new IllegalArgumentException("Priority must be positive");
        }

        if (money == null) {
            throw new IllegalArgumentException("PriceMoney must not be null");
        }

        return new Price(id, brandId, startDate, endDate, priceList, productId, money);
    }
}
