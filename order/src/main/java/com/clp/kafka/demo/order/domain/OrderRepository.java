package com.clp.kafka.demo.order.domain;

import com.example.eventcommons.event.order.PricingOrderSnapshot;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Objects;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<PricingOrder, Long> {
    default PricingOrderSnapshot createOrder(String groupId) {
        return saveAndFlush(new PricingOrder(groupId)).snapshot();
    }
}
