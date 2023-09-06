package com.clp.kafka.demo.customer.service.port;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ExternalSystemCustomerGroup {
    private String groupId;
    Set<ExternalSystemCustomer> customers;
}
