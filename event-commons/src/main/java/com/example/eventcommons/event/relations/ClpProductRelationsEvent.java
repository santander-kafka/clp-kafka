package com.example.eventcommons.event.relations;

import com.example.eventcommons.event.global.ClpEvent;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

abstract class ClpProductRelationsEvent implements ClpEvent {
    @JsonIgnore
    public abstract Long getOrderId();

    @JsonIgnore
    public String getDefaultTopic() {
        return getTopicTemplate() + getOrderId().toString();
    }
}
