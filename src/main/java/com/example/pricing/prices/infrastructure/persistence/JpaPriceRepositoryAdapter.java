package com.example.pricing.prices.infrastructure.persistence;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.pricing.prices.domain.Price;
import com.example.pricing.prices.domain.PriceRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JpaPriceRepositoryAdapter implements PriceRepository {
    private final JpaPriceRepository jpaRepository;

    @Override
    public List<Price> findByBrandAndProductAndDate(long brandId, long productId, LocalDateTime date) {
        return jpaRepository.findActive(brandId, productId, date)
                .stream()
                .map(PriceEntity::toDomain)
                .toList();
    }

}
