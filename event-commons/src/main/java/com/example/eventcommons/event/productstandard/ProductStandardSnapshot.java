package com.example.eventcommons.event.productstandard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProductStandardSnapshot {
    private Long id;
    private boolean standard;
}
