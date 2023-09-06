package com.clp.kafka.demo.customer.domain;

import com.clp.kafka.demo.customer.domain.model.ClpCustomerGroup;
import com.clp.kafka.demo.customer.service.port.CustomerRepositoryPort;
import com.clp.kafka.demo.customer.service.port.ExternalSystemCustomerGroup;
import com.example.eventcommons.event.customer.CustomerGroupSnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomerRepository implements CustomerRepositoryPort {
    private final CustomerCrudRepository repository;

    @Override
    public CustomerGroupSnapshot upsertGroup(ExternalSystemCustomerGroup source) {
        repository.deleteAllByGroupId(source.getGroupId());
        return repository.save(ClpCustomerGroup.from(source)).snapshot();
    }
}
