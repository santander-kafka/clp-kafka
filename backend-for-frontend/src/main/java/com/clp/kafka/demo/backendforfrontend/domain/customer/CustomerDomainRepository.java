package com.clp.kafka.demo.backendforfrontend.domain.customer;

import com.example.eventcommons.event.customer.CustomerGroupSnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomerDomainRepository {
    private final CustomerDtoRepository customerDtoRepository;

    @CachePut(value = "customerCache", key = "#snapshot.groupId")
    public CustomerGroupDto upsert(CustomerGroupSnapshot snapshot) {
        return customerDtoRepository.upsert(snapshot);
    }

    @Cacheable(value = "customerCache", key = "#groupId")
    public CustomerGroupDto getByGroupId(String groupId) {
        return customerDtoRepository.getByGroupId(groupId);
    }
}
