package com.clp.kafka.demo.backendforfrontend.domain.decision;

import com.example.eventcommons.event.decision.event.DecisionSnapshot;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

import static java.util.Optional.ofNullable;

public interface DecisionRepository extends MongoRepository<DecisionDto, Long> {
    default DecisionDto upsert(DecisionSnapshot decisionSnapshot) {
        validateNotArchival(decisionSnapshot.getOrderId());
        deleteAllByOrderId(decisionSnapshot.getOrderId());
        return save(DecisionDto.from(decisionSnapshot));
    }

    default DecisionDto archive(DecisionSnapshot decisionSnapshot) {
        validateNotArchival(decisionSnapshot.getOrderId());
        deleteAllByOrderId(decisionSnapshot.getOrderId());
        return save(DecisionDto.archive(decisionSnapshot));
    }

    default void validateNotArchival(Long orderId) {
        ofNullable(findByOrderId(orderId)).map(DecisionDto::getArchival).ifPresent(isArchival -> {
            if (isArchival) {
                throw new RuntimeException("Archival");
            }
        });
    }

    DecisionDto findByOrderId(Long orderId);

    List<DecisionDto> findAllByOrderIdIn(List<Long> orderIds);

    void deleteAllByOrderId(Long orderId);

}
