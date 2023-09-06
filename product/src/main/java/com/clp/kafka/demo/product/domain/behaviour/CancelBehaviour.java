package com.clp.kafka.demo.product.domain.behaviour;

import com.clp.kafka.demo.product.domain.WithSnapshot;

public interface CancelBehaviour extends WithSnapshot {
    WithSnapshot cancel();
}
