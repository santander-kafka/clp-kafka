package com.example.eventcommons.event.decision.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClpDecisionChangedEvent implements ClpDecisionEvent {
    @Builder.Default
    private UUID id = UUID.randomUUID();
    private UUID sagaId;

    @Builder.Default
    private DecisionEventType type = DecisionEventType.DECISION_CHANGED;

    private DecisionSnapshot snapshot;

    @Override
    public Long getOrderId() {
        return snapshot.getOrderId();
    }
}
