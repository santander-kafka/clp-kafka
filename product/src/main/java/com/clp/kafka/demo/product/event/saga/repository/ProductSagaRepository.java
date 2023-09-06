package com.clp.kafka.demo.product.event.saga.repository;

import com.clp.kafka.demo.product.event.saga.model.DefaultProductSaga;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import static java.util.Optional.ofNullable;

public interface ProductSagaRepository extends JpaRepository<DefaultProductSaga, Long> {

    default void updateSaga(UUID processId, Function<DefaultProductSaga, DefaultProductSaga> consumer) {
        ofNullable(getByProcessId(processId)).ifPresent(consumer::apply);
    }

    default DefaultProductSaga getProcessState(UUID processId) {
        return findByProcessId(processId)
                .orElseThrow();
    }

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    DefaultProductSaga getByProcessId(UUID processId);

    Optional<DefaultProductSaga> findByProcessId(UUID processId);
}
