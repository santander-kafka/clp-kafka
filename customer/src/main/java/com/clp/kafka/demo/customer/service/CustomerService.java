package com.clp.kafka.demo.customer.service;

import com.clp.kafka.demo.customer.service.port.CustomerRepositoryPort;
import com.clp.kafka.demo.customer.service.port.ExternalSystemCustomerGroup;
import com.example.eventcommons.event.customer.CustomerGroupSnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomerService {
    private final CustomerRepositoryPort customerRepository;
    private final ExternalCustomerRestClient externalCustomerRestClient;

    @Transactional
    public CustomerGroupSnapshot upsertGroup(String groupId) {
        ExternalSystemCustomerGroup sourceGroup = externalCustomerRestClient.get(groupId);
        return customerRepository.upsertGroup(sourceGroup);
    }
}
