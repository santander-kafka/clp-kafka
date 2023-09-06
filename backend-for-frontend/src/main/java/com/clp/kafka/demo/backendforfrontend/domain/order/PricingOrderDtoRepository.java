package com.clp.kafka.demo.backendforfrontend.domain.order;

import com.example.eventcommons.event.order.PricingOrderSnapshot;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PricingOrderDtoRepository extends MongoRepository<PricingOrderBaseDto, Long> {

    default PricingOrderBaseDto upsert(PricingOrderSnapshot orderSnapshot) {
        deleteAllById(orderSnapshot.getId());
        return save(PricingOrderBaseDto.from(orderSnapshot));
    }

    default PricingOrderBaseDto getById(Long orderId) {
        return findById(orderId).orElseThrow();
    }

    Optional<PricingOrderBaseDto> findById(Long orderId);

    List<PricingOrderBaseDto> findAllByGroupId(String groupId);

    void deleteAllById(Long orderId);
}
