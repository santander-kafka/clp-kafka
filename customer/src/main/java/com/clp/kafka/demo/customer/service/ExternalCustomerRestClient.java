package com.clp.kafka.demo.customer.service;

import com.clp.kafka.demo.customer.service.port.ExternalSystemCustomer;
import com.clp.kafka.demo.customer.service.port.ExternalSystemCustomerGroup;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ExternalCustomerRestClient {
    public ExternalSystemCustomerGroup get(String groupId) {
        return ExternalSystemCustomerGroup.builder()
                .groupId(groupId)
                .customers(Set.of(
                        ExternalSystemCustomer.builder().id("123").name("Wiśniewski").build(),
                        ExternalSystemCustomer.builder().id("234").name("Kowalski").build()
                ))
                .build();
    }
}
