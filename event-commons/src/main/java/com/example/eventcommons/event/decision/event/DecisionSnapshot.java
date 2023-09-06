package com.example.eventcommons.event.decision.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DecisionSnapshot {
    private Long orderId;
    private String result;
    private List<String> errors;
}
