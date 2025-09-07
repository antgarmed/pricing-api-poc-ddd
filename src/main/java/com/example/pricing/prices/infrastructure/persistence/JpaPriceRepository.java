package com.example.pricing.prices.infrastructure.persistence;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaPriceRepository extends JpaRepository<PriceEntity, Long> {
    @Query("""
              SELECT p FROM PriceEntity p
               WHERE p.brandId = :brandId
                 AND p.productId = :productId
                 AND p.startDate <= :at AND :at < p.endDate
               ORDER BY p.priority DESC, p.startDate DESC, p.priceList DESC
            """)
    List<PriceEntity> findActive(
            @Param("brandId") long brandId,
            @Param("productId") long productId,
            @Param("at") LocalDateTime at);
}
