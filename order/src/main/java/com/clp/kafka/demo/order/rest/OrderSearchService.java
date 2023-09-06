package com.clp.kafka.demo.order.rest;

import com.clp.kafka.demo.order.event.saga.create.OrderCreationSaga;
import com.clp.kafka.demo.order.event.saga.create.OrderCreationSagaRepository;
import com.clp.kafka.demo.order.event.OrderSagaState;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderSearchService {
    private final OrderCreationSagaRepository orderSagaRepository;

    public OrderSagaState getProcessState(UUID processId) {
        return orderSagaRepository.getProcessState(processId);
    }
}
