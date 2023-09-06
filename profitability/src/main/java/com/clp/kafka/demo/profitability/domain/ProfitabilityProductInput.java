package com.clp.kafka.demo.profitability.domain;

import com.example.eventcommons.event.product.snapshot.GuaranteeLimitSnapshot;
import com.example.eventcommons.event.product.snapshot.GuaranteeProductSnapshot;
import com.example.eventcommons.event.product.snapshot.OverdraftSnapshot;
import com.example.eventcommons.event.product.snapshot.ProductSnapshot;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ProfitabilityProductInput {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long productId;
    private String type;
    private BigDecimal amount;

    public static ProfitabilityProductInput toProduct(ProductSnapshot snapshot) {
        return switch (snapshot.getType()) {
            case "OVERDRAFT" -> ProfitabilityProductInput.builder()
                    .productId(snapshot.getId())
                    .amount(((OverdraftSnapshot) snapshot).getAmount())
                    .type("OVERDRAFT")
                    .build();
            case "GUARANTEE_LIMIT" -> ProfitabilityProductInput.builder()
                    .productId(snapshot.getId())
                    .amount(((GuaranteeLimitSnapshot) snapshot).getAmount())
                    .type("GUARANTEE_LIMIT")
                    .build();
            case "GUARANTEE_PRODUCT" -> ProfitabilityProductInput.builder()
                    .productId(snapshot.getId())
                    .amount(((GuaranteeProductSnapshot) snapshot).getAmount())
                    .type("GUARANTEE_PRODUCT")
                    .build();
            default -> null;
        };
    }
}
