package com.example.eventcommons.event.product.event;

import com.example.eventcommons.event.product.snapshot.ProductSnapshot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ClpProductCanceledEvent implements ClpProductEvent {
    private UUID id;
    public UUID sagaId;

    @Builder.Default
    private ProductEventType type = ProductEventType.PRODUCT_CANCELED;
    private ProductSnapshot targetSnapshot;
}
