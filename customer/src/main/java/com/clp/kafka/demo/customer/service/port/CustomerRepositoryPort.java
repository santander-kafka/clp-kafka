package com.clp.kafka.demo.customer.service.port;


import com.example.eventcommons.event.customer.CustomerGroupSnapshot;

public interface CustomerRepositoryPort {
    CustomerGroupSnapshot upsertGroup(ExternalSystemCustomerGroup group);
}
