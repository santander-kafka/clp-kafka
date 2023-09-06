package com.example.eventcommons.event.profitability.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ClpProfitabilityCalculatedEvent implements ClpProfitabilityEvent {
    @Builder.Default
    private UUID id = UUID.randomUUID();
    private UUID sagaId;

    @Builder.Default
    private ProfitabilityEventType type = ProfitabilityEventType.PROFITABILITY_CALCULATED;

    private ProfitabilitySnapshot snapshot;
}
