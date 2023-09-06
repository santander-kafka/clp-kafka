package com.example.eventcommons.event.profitability.event;

import com.example.eventcommons.event.global.ClpEvent;
import com.fasterxml.jackson.annotation.JsonIgnore;

import static java.util.Optional.ofNullable;

public interface ClpProfitabilityEvent extends ClpEvent {
    ProfitabilitySnapshot getSnapshot();

    @JsonIgnore
    default Long getOrderId() {
        return ofNullable(getSnapshot()).map(ProfitabilitySnapshot::getOrderId).orElse(null);
    }

    @JsonIgnore
    @Override
    default String getDefaultTopic() {
        return getTopicTemplate() + getOrderId().toString();
    }
}
