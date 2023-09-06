package com.example.eventcommons.event.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class PricingOrderSnapshot {
    Long id;
    String groupId;
}
