package com.example.eventcommons.event.customer.event;

import com.example.eventcommons.event.customer.CustomerGroupSnapshot;
import com.example.eventcommons.event.product.event.ProductEventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ClpCustomerGroupUpdatedEvent implements ClpCustomerEvent {
    @Builder.Default
    private UUID id = UUID.randomUUID();
    public UUID sagaId;

    @Builder.Default
    private CustomerEventType type = CustomerEventType.CUSTOMER_UPDATED;
    private CustomerGroupSnapshot snapshot;
    private CustomerGroupSnapshot beforeSnapshot;

    @Override
    public String getDefaultTopic() {
        return "CLP_CUSTOMERS_CHANGES";
    }
}
