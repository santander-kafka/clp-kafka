package com.clp.kafka.demo.backendforfrontend.domain.customer;

import com.example.eventcommons.event.customer.CustomerGroupSnapshot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerGroupDto implements Serializable {
    @Id
    private String id;
    private String groupId;

    private Set<CustomerDto> customers;

    public static CustomerGroupDto from(CustomerGroupSnapshot snapshot) {
        return CustomerGroupDto.builder()
                .groupId(snapshot.getGroupId())
                .customers(snapshot.getCustomers().stream().map(customer -> CustomerDto.builder()
                                .customerId(customer.getId())
                                .name(customer.getName())
                                .build())
                        .collect(Collectors.toSet()))
                .build();
    }
}
