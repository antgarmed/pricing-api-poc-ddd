package com.example.pricing.prices.infrastructure.persistence;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.pricing.prices.domain.Price;
import com.example.pricing.prices.domain.PriceMoney;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "prices")
@Data
public class PriceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "brand_id", nullable = false)
    private Long brandId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "price_list", nullable = false)
    private Long priceList;

    @Column(name = "priority", nullable = false)
    private Long priority;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "curr", nullable = false, length = 3)
    private String currency;

    public static Price toDomain(PriceEntity entity) {
        return Price.of(
                entity.getId(),
                entity.getBrandId(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getPriceList(),
                entity.getPriority(),
                entity.getProductId(),
                PriceMoney.of(entity.getPrice(), entity.getCurrency()));
    }
}
