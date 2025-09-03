package com.example.prices.prices.domain;

import java.time.LocalDateTime;
import java.util.List;

public interface PriceRepository {
    List<Price> findByBrandAndProductAndDate(long brandId, long productId, LocalDateTime date);
}
