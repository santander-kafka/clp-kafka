package com.clp.kafka.demo.backendforfrontend.domain.profitability;

import com.example.eventcommons.event.profitability.event.ProfitabilitySnapshot;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProfitabilityDtoRepository extends MongoRepository<ProfitabilityDto, Long> {
    default ProfitabilityDto upsert(ProfitabilitySnapshot snapshot) {
        deleteAllByOrderId(snapshot.getOrderId());
        return save(ProfitabilityDto.from(snapshot));
    }
    List<ProfitabilityDto> getByOrderIdIn(List<Long> orderIds);

    ProfitabilityDto getByOrderId(Long orderId);

    void deleteAllByOrderId(Long orderId);
}
