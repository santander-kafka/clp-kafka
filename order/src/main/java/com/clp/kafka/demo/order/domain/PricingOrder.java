package com.clp.kafka.demo.order.domain;

import com.example.eventcommons.event.order.PricingOrderSnapshot;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter(AccessLevel.PACKAGE)
class PricingOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String groupId;

    public PricingOrder(String groupId) {
        this.groupId = groupId;
    }

    PricingOrderSnapshot snapshot() {
        return PricingOrderSnapshot.builder()
                .id(getId())
                .groupId(getGroupId())
                .build();
    }
}
