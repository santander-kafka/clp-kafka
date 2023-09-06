package com.clp.kafka.demo.customer.domain.model;

import com.clp.kafka.demo.customer.service.port.ExternalSystemCustomerGroup;
import com.example.eventcommons.event.customer.CustomerGroupSnapshot;
import com.example.eventcommons.event.customer.CustomerSnapshot;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ClpCustomerGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String groupId;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    Set<ClpCustomer> customers;

    public static ClpCustomerGroup from(ExternalSystemCustomerGroup source) {
        return ClpCustomerGroup.builder()
                .groupId(source.getGroupId())
                .customers(source.getCustomers().stream()
                        .map(customer -> ClpCustomer.builder()
                                .customerId(customer.getId())
                                .name(customer.getName())
                                .build())
                        .collect(Collectors.toSet()))
                .build();
    }

    public CustomerGroupSnapshot snapshot() {
        return CustomerGroupSnapshot.builder()
                .groupId(getGroupId())
                .customers(getCustomers().stream()
                        .map(customer -> CustomerSnapshot.builder()
                                .id(customer.getCustomerId())
                                .name(customer.getName())
                                .build())
                        .collect(Collectors.toSet()))
                .build();

    }
}
