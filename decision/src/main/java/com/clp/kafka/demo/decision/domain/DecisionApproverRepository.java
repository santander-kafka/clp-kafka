package com.clp.kafka.demo.decision.domain;

import com.example.eventcommons.event.decision.event.DecisionSnapshot;
import com.example.eventcommons.event.order.event.ClpOrderCreatedEvent;
import com.example.eventcommons.event.product.event.ClpProductCreatedEvent;
import com.example.eventcommons.event.product.event.ClpProductModificationCreatedEvent;
import com.example.eventcommons.event.product.event.ClpProductRemovedEvent;
import com.example.eventcommons.event.product.event.ClpProductUpdatedEvent;
import com.example.eventcommons.event.productstandard.ClpProductStandardChangedEvent;
import com.example.eventcommons.event.profitability.event.ClpProfitabilityCalculatedEvent;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface DecisionApproverRepository extends JpaRepository<DecisionApprover, Long> {
    @Transactional
    default DecisionSnapshot approveDecision(Long orderId) {
        return getByOrderId(orderId).approve();
    }

    @Transactional
    default DecisionSnapshot rejectDecision(Long orderId) {
        return getByOrderId(orderId).reject();
    }

    @Transactional
    default DecisionSnapshot onOrderCreated(ClpOrderCreatedEvent event) {
        return saveAndFlush(findByOrderId(event.getOrderId()).orElse(DecisionApprover.createFrom(event))).snapshot();
    }

    @Transactional
    default DecisionSnapshot onProductCreated(ClpProductCreatedEvent event) {
        return getByOrderId(event.getOrderId()).onProductCreated(event);
    }

    @Transactional
    default DecisionSnapshot onProductUpdated(ClpProductUpdatedEvent event) {
        return getByOrderId(event.getOrderId()).onProductUpdated(event);
    }

    @Transactional
    default DecisionSnapshot onProductRemoved(ClpProductRemovedEvent event) {
        return getByOrderId(event.getOrderId()).onProductRemoved(event);
    }

    @Transactional
    default DecisionSnapshot onProductModified(ClpProductModificationCreatedEvent event) {
        return getByOrderId(event.getOrderId()).onProductModified(event);
    }

    @Transactional
    default DecisionSnapshot onProductStandardChanged(ClpProductStandardChangedEvent event) {
        return getByOrderId(event.getOrderId()).onProductStandardChanged(event);
    }

    @Transactional
    default DecisionSnapshot onProfitabilityCalculated(ClpProfitabilityCalculatedEvent event) {
        return getByOrderId(event.getOrderId()).onProfitabilityCalculated(event);
    }

    Optional<DecisionApprover> findByOrderId(Long orderId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    DecisionApprover getByOrderId(Long orderId);
}
