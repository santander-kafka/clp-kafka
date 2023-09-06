package com.example.eventcommons.event.decision.event;

import com.example.eventcommons.event.global.ClpOrderRelatedEvent;
import com.fasterxml.jackson.annotation.JsonIgnore;

public interface ClpDecisionEvent extends ClpOrderRelatedEvent {

    @JsonIgnore
    @Override
    default String getDefaultTopic() {
        return getTopicTemplate() + getOrderId().toString();
    }
}
