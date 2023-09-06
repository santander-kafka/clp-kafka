package com.example.eventcommons.event.product.snapshot;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GuaranteeLimitSnapshot implements ProductSnapshot {
    private Long id;
    private String customerId;
    private Long orderId;
    private String type;
    private BigDecimal amount;
    private Integer commission;
    private LocalDate beginDate;
    private LocalDate maturityDate;
    private Set<Long> absorbedProductIds;
}
