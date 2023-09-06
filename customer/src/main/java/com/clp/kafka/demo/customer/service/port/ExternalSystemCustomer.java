package com.clp.kafka.demo.customer.service.port;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ExternalSystemCustomer {
    private String id;
    private String name;
}
