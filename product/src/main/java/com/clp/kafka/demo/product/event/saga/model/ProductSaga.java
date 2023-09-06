package com.clp.kafka.demo.product.event.saga.model;

import java.util.UUID;

public interface ProductSaga {
    UUID getProcessId();

    ProductSagaState getState();
}
