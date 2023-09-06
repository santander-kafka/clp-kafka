package com.example.eventcommons.event.product.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GuaranteeLimitForm implements ProductForm {
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
