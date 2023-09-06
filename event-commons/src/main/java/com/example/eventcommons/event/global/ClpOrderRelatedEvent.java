package com.example.eventcommons.event.global;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface ClpOrderRelatedEvent extends ClpEvent {
    @JsonIgnore
    Long getOrderId();
}
