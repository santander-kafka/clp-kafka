package com.clp.kafka.demo.decision.domain;

import com.example.eventcommons.event.decision.event.DecisionSnapshot;
import com.example.eventcommons.event.order.event.ClpOrderCreatedEvent;
import com.example.eventcommons.event.product.event.ClpProductCreatedEvent;
import com.example.eventcommons.event.product.event.ClpProductModificationCreatedEvent;
import com.example.eventcommons.event.product.event.ClpProductRemovedEvent;
import com.example.eventcommons.event.product.event.ClpProductUpdatedEvent;
import com.example.eventcommons.event.productstandard.ClpProductStandardChangedEvent;
import com.example.eventcommons.event.profitability.event.ClpProfitabilityCalculatedEvent;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class DecisionApprover {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long orderId;
    @Enumerated(EnumType.STRING)
    private Result result;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<Long> productIds = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<Long> nonStandardProductIds = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> pricings = new ArrayList<>();

    public DecisionSnapshot approve() {
        if (result != Result.NEW) {
            throw new RuntimeException("Already: " + result);
        }
        if (this.getErrors().size() > 0) {
            throw new RuntimeException(String.join(" , ", this.getErrors()));
        }
        result = Result.APPROVED;
        return snapshot();
    }

    public DecisionSnapshot reject() {
        if (result != Result.NEW) {
            throw new RuntimeException("Already: " + result);
        }
        result = Result.REJECTED;
        return snapshot();
    }

    public static DecisionApprover createFrom(ClpOrderCreatedEvent event) {
        return DecisionApprover.builder()
                .result(Result.NEW)
                .orderId(event.getOrderId())
                .build();
    }

    public DecisionSnapshot onProductCreated(ClpProductCreatedEvent event) {
        if (result != Result.NEW) {
            throw new RuntimeException("Already: " + result);
        }
        productIds.add(event.getTargetSnapshot().getId());
        return snapshot();
    }

    public DecisionSnapshot onProductUpdated(ClpProductUpdatedEvent event) {
        // TODO
        return snapshot();
    }

    public DecisionSnapshot onProductRemoved(ClpProductRemovedEvent event) {
        productIds.removeIf(id -> Objects.equals(event.getTargetSnapshot().getId(), id));
        return snapshot();
    }

    public DecisionSnapshot onProductModified(ClpProductModificationCreatedEvent event) {
        if (result != Result.NEW) {
            throw new RuntimeException("Already: " + result);
        }
        productIds.add(event.getTargetSnapshot().getId());
        return snapshot();
    }

    public DecisionSnapshot onProductStandardChanged(ClpProductStandardChangedEvent event) {
        if (result != Result.NEW) {
            throw new RuntimeException("Already: " + result);
        }
        event.getProductStandardSnapshots().forEach(standardSnapshot -> {
            nonStandardProductIds.removeIf(id -> standardSnapshot.getId().equals(id));
            if (!standardSnapshot.isStandard()) {
                nonStandardProductIds.add(standardSnapshot.getId());
            }
        });

        return snapshot();
    }

    public DecisionSnapshot onProfitabilityCalculated(ClpProfitabilityCalculatedEvent event) {
        if (result != Result.NEW) {
            throw new RuntimeException("Already: " + result);
        }
        pricings.clear();
        pricings.addAll(new ArrayList<>(event.getSnapshot().getPricings()));

        return snapshot();
    }

    private List<String> getErrors() {
        List<String> errors = new ArrayList<>();
        if (productIds.size() < 1) {
            errors.add("No products");
        }
        if (productIds.size() != pricings.size() || pricings.stream().anyMatch(pricing -> pricing.equals("RED"))) {
            errors.add("Pricings: " + pricings + " - Product ids: " + productIds);
        }
        if (nonStandardProductIds.size() > 1) {
            errors.add("Non standards: " + nonStandardProductIds);
        }
        return errors;
    }

    DecisionSnapshot snapshot() {
        return DecisionSnapshot.builder()
                .orderId(orderId)
                .result(result.name())
                .errors(getErrors())
                .build();
    }

    public enum Result {
        NEW,
        APPROVED,
        REJECTED,
    }
}
