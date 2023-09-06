package com.example.eventcommons.event.order.event;

import com.example.eventcommons.event.global.ClpEvent;
import com.example.eventcommons.event.global.ClpOrderRelatedEvent;
import com.example.eventcommons.event.order.PricingOrderSnapshot;
import com.fasterxml.jackson.annotation.JsonIgnore;


public interface ClpOrderEvent extends ClpOrderRelatedEvent {
    PricingOrderSnapshot getSnapshot();

    @JsonIgnore
    default Long getOrderId() {
        return getSnapshot().getId();
    }

    @JsonIgnore
    @Override
    default String getDefaultTopic() {
        return getTopicTemplate() + getSnapshot().getId().toString();
    }
}
