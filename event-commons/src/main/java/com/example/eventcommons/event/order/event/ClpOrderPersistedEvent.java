package com.example.eventcommons.event.order.event;

import com.example.eventcommons.event.order.PricingOrderSnapshot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ClpOrderPersistedEvent implements ClpOrderEvent {
    @Builder.Default
    private UUID id = UUID.randomUUID();
    private UUID sagaId;

    @Builder.Default
    private OrderEventType type = OrderEventType.ORDER_PERSISTED;

    PricingOrderSnapshot snapshot;
}
