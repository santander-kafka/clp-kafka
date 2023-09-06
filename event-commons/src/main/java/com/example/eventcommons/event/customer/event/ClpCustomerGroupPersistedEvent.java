package com.example.eventcommons.event.customer.event;

import com.example.eventcommons.event.customer.CustomerGroupSnapshot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ClpCustomerGroupPersistedEvent implements ClpCustomerEvent {
    @Builder.Default
    private UUID id = UUID.randomUUID();
    public UUID sagaId;

    @Builder.Default
    private CustomerEventType type = CustomerEventType.CUSTOMER_PERSISTED;
    private CustomerGroupSnapshot snapshot;

    @Override
    public String getDefaultTopic() {
        return "CLP_CUSTOMERS_CHANGES";
    }
}
