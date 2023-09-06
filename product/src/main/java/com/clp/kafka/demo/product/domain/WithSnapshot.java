package com.clp.kafka.demo.product.domain;

import com.example.eventcommons.event.product.snapshot.ProductSnapshot;

public interface WithSnapshot {
    <T extends ProductSnapshot> T snapshot();
}
