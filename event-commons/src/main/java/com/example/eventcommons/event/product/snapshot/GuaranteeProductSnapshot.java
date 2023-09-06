package com.example.eventcommons.event.product.snapshot;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GuaranteeProductSnapshot implements ProductSnapshot {
    private Long id;
    private String customerId;
    private Long orderId;
    private String type;
    private BigDecimal amount;
    private Integer commission;
    private LocalDate beginDate;
    private LocalDate maturityDate;
}
