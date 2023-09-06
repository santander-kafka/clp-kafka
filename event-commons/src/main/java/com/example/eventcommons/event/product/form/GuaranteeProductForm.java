package com.example.eventcommons.event.product.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GuaranteeProductForm implements ProductForm {
    private Long id;
    private String customerId;
    private String type;
    private Long orderId;
    private BigDecimal amount;
    private Integer commission;
    private LocalDate beginDate;
    private LocalDate maturityDate;
}
