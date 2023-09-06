package com.clp.kafka.demo.productstandard.domain.model;

import com.clp.kafka.demo.productstandard.domain.input.DefaultProductStandardInput;
import com.example.eventcommons.event.productstandard.ProductStandardSnapshot;
import org.springframework.stereotype.Service;

import java.util.Set;

import static java.util.Optional.ofNullable;

@Service
public class ProductStandardCalculator {
    public Set<ProductStandardSnapshot> calculateStandard(DefaultProductStandardInput input) {
        return Set.of(ProductStandardSnapshot.builder()
                .id(input.getProductId())
                .standard(isStandard(input))
                .build());
    }

    private boolean isStandard(DefaultProductStandardInput input) {
        // fetch config
        return ofNullable(input.getCommission())
                .map(commission -> commission < 3)
                .orElse(true);
    }
}
