package com.clp.kafka.demo.product.domain.behaviour;

import com.clp.kafka.demo.product.domain.WithSnapshot;

public interface ModifyBehaviour extends WithSnapshot {
    WithSnapshot modify();
}
