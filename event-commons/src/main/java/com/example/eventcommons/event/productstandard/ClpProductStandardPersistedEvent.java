package com.example.eventcommons.event.productstandard;

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
public class ClpProductStandardPersistedEvent implements ClpProductStandardEvent {
    @Builder.Default
    private UUID id = UUID.randomUUID();
    private UUID sagaId;
    private Long orderId;

    @Builder.Default
    private ProductEventType type = ProductEventType.PRODUCT_STANDARD_PERSISTED;

    Set<ProductStandardSnapshot> productStandardSnapshots;
}
