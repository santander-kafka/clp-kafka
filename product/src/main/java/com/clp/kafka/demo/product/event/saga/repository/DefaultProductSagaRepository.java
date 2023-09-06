package com.clp.kafka.demo.product.event.saga.repository;

import com.clp.kafka.demo.product.event.saga.model.DefaultProductSaga;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultProductSagaRepository {
    private final ProductSagaRepository repository;

    @Transactional
    public UUID start() {
        DefaultProductSaga saga = DefaultProductSaga.start();
        return repository.saveAndFlush(saga)
                .getProcessId();
    }

    @Transactional
    public void successProductPersisted(UUID sagaId) {
        repository.updateSaga(sagaId,
                DefaultProductSaga::successProductPersisted);
    }

    @Transactional
    public void successProductRelationsPersisted(UUID sagaId) {
        repository.updateSaga(sagaId,
                DefaultProductSaga::successProductRelationsPersisted);
    }

    @Transactional
    public void successProductStandardPersisted(UUID sagaId) {
        repository.updateSaga(sagaId,
                DefaultProductSaga::successProductStandardPersisted);
    }

    @Transactional
    public void decisionUpdated(UUID sagaId) {
        repository.updateSaga(sagaId,
                DefaultProductSaga::decisionUpdated);
    }

    @Transactional
    public void profitabilityInputPersisted(UUID sagaId) {
        repository.updateSaga(sagaId,
                DefaultProductSaga::profitabilityInputPersisted);
    }

    public DefaultProductSaga getProcessState(UUID processId) {
        return repository.getProcessState(processId);
    }
}