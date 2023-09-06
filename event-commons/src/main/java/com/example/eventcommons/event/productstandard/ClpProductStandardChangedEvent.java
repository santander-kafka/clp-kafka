package com.example.eventcommons.event.productstandard;

import com.example.eventcommons.event.global.ClpEvent;
import com.example.eventcommons.event.product.event.ProductEventType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ClpProductStandardChangedEvent implements ClpProductStandardEvent {
    @Builder.Default
    private UUID id = UUID.randomUUID();
    private UUID sagaId;
    private Long pricingOrderId;

    @Builder.Default
    private ProductEventType type = ProductEventType.PRODUCT_STANDARD_CHANGED;

    Set<ProductStandardSnapshot> productStandardSnapshots;

    @Override
    public Long getOrderId() {
        return pricingOrderId;
    }
}
