package com.clp.kafka.demo.product.domain.behaviour;

import com.clp.kafka.demo.product.domain.WithSnapshot;

public interface ResignBehaviour extends WithSnapshot {
    WithSnapshot resign();
}
