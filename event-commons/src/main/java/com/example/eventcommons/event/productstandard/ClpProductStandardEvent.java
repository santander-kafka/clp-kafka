package com.example.eventcommons.event.productstandard;

import com.example.eventcommons.event.global.ClpEvent;
import com.example.eventcommons.event.global.ClpOrderRelatedEvent;
import com.fasterxml.jackson.annotation.JsonIgnore;

public interface ClpProductStandardEvent extends ClpOrderRelatedEvent {
    Long getOrderId();

    @JsonIgnore
    @Override
    default String getDefaultTopic() {
        return getTopicTemplate() + getOrderId().toString();
    }
}
