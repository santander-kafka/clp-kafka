package com.example.eventcommons.event.relations;

import com.example.eventcommons.event.global.ClpOrderRelatedEvent;
import com.example.eventcommons.event.product.event.ProductEventType;
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
public class ClpProductRelationsChangedEvent extends ClpProductRelationsEvent implements ClpOrderRelatedEvent {
    @Builder.Default
    private UUID id = UUID.randomUUID();
    private UUID sagaId;
    private Long pricingOrderId;

    @Builder.Default
    private ProductEventType type = ProductEventType.PRODUCT_RELATIONS_CHANGED;

    Set<ProductRelationSnapshot> productRelationSnapshots;

    @Override
    public Long getOrderId() {
        return pricingOrderId;
    }
}
