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
public class ClpDecisionPersistedEvent implements ClpDecisionEvent {
    @Builder.Default
    private UUID id = UUID.randomUUID();
    public UUID sagaId;

    @Builder.Default
    private DecisionEventType type = DecisionEventType.DECISION_PERSISTED;

    private DecisionSnapshot snapshot;

    @Override
    public Long getOrderId() {
        return snapshot.getOrderId();
    }
}
