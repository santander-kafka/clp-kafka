package com.clp.kafka.demo.backendforfrontend.domain.customer;

import com.example.eventcommons.event.customer.CustomerGroupSnapshot;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerDtoRepository extends MongoRepository<CustomerGroupDto, Long> {
    default CustomerGroupDto upsert(CustomerGroupSnapshot snapshot) {
        deleteAllByGroupId(snapshot.getGroupId());
        return save(CustomerGroupDto.from(snapshot));
    }

    CustomerGroupDto getByGroupId(String groupId);

    void deleteAllByGroupId(String groupId);
}
