package com.example.eventcommons.event.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CustomerGroupSnapshot {
    private String groupId;
    private Set<CustomerSnapshot> customers;
}
