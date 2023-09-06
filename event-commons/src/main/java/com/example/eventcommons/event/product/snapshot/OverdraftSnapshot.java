package com.example.eventcommons.event.product.snapshot;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OverdraftSnapshot implements ProductSnapshot {
    private Long id;
    private String customerId;
    private Long orderId;
    private String type;
    private BigDecimal amount;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate beginDate;
    private LocalDate maturityDate;
}
