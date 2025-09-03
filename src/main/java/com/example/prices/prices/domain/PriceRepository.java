package com.example.prices.prices.domain;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PriceRepository {
    Optional<Price> findByBrandAndProductAndDate(long brandId, long productId, LocalDateTime date);
}
