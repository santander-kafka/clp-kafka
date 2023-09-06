package com.example.eventcommons.event.product.event;

import com.example.eventcommons.event.product.snapshot.ProductSnapshot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ClpProductCreatedEvent implements ClpProductEvent {
    @Builder.Default
    private UUID id = UUID.randomUUID();
    private UUID sagaId;

    @Builder.Default
    private ProductEventType type = ProductEventType.PRODUCT_CREATED;
    private ProductSnapshot targetSnapshot;
}
