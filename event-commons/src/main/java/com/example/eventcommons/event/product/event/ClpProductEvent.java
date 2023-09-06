package com.example.eventcommons.event.product.event;

import com.example.eventcommons.event.global.ClpEvent;
import com.example.eventcommons.event.global.ClpOrderRelatedEvent;
import com.example.eventcommons.event.product.snapshot.ProductSnapshot;
import com.fasterxml.jackson.annotation.JsonIgnore;

public interface ClpProductEvent extends ClpOrderRelatedEvent {
    ProductSnapshot getTargetSnapshot();

    @Override
    default Long getOrderId() {
        return getTargetSnapshot().getOrderId();
    }

    @JsonIgnore
    @Override
    default String getDefaultTopic() {
        return getTopicTemplate() + getTargetSnapshot().getOrderId().toString();
    }
}
