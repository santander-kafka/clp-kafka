package com.example.eventcommons.event.profitability.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProfitabilitySnapshot {
    private Long orderId;
    @Builder.Default
    private List<String> pricings = new ArrayList<>();
}
