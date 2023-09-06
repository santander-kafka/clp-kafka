package com.clp.kafka.demo.customer.domain;

import com.clp.kafka.demo.customer.domain.model.ClpCustomerGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CustomerCrudRepository extends JpaRepository<ClpCustomerGroup, Long> {
    void deleteAllByGroupId(String groupId);
}
