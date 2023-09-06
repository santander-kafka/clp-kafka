package com.clp.kafka.demo.order.event.saga.create;

import com.clp.kafka.demo.order.event.OrderSagaState;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderCreationSagaRepository extends JpaRepository<OrderCreationSaga, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    OrderCreationSaga getByProcessId(UUID processId);

    Optional<OrderCreationSaga> findByProcessId(UUID processId);

    default OrderSagaState getProcessState(UUID processId) {
        return findByProcessId(processId).orElseThrow()
                .getState();
    }

    @Transactional
    default UUID startSaga() {
        UUID uuid = UUID.randomUUID();
        save(OrderCreationSaga.start(uuid));
        return uuid;
    }

    @Transactional
    default void successOrderCreated(UUID processId) {
        getByProcessId(processId)
                .successOrderCreated();
    }

    @Transactional
    default void successOrderPersisted(UUID processId) {
        getByProcessId(processId)
                .successOrderPersisted();
    }

}