package com.clp.kafka.demo.product.service;


import com.clp.kafka.demo.product.event.saga.model.DefaultProductSaga;
import com.clp.kafka.demo.product.event.saga.repository.DefaultProductSagaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductSearchService {
    private final DefaultProductSagaRepository sagaRepository;

    public DefaultProductSaga getProcessState(UUID processId) {
        return sagaRepository.getProcessState(processId);
    }
}
