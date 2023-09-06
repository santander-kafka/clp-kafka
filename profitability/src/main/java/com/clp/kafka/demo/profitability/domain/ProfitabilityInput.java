package com.clp.kafka.demo.profitability.domain;

import com.clp.kafka.demo.profitability.event.ProfitabilityEvents;
import com.example.eventcommons.event.global.ClpEvent;
import com.example.eventcommons.event.product.event.ClpProductCreatedEvent;
import com.example.eventcommons.event.product.event.ClpProductRemovedEvent;
import com.example.eventcommons.event.product.event.ClpProductUpdatedEvent;
import com.example.eventcommons.event.product.event.ProductEventType;
import com.example.eventcommons.event.product.snapshot.GuaranteeLimitSnapshot;
import com.example.eventcommons.event.product.snapshot.GuaranteeProductSnapshot;
import com.example.eventcommons.event.product.snapshot.OverdraftSnapshot;
import com.example.eventcommons.event.product.snapshot.ProductSnapshot;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@ToString
public class ProfitabilityInput {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long orderId;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<ProfitabilityProductInput> products = new HashSet<>();

    public static ProfitabilityInput from(Long orderId, ProfitabilityEvents events) {
        ProfitabilityInput profitabilityInput = new ProfitabilityInput();
        profitabilityInput.orderId = orderId;
        events.getEvents().forEach(profitabilityInput::onCreatedEvent);
        events.getEvents().forEach(profitabilityInput::onUpdatedEvent);
        events.getEvents().forEach(profitabilityInput::onRemovedEvent);
        return profitabilityInput;
    }

    private void onCreatedEvent(ClpEvent event) {
        if ((ProductEventType.PRODUCT_CREATED.equals(event.getType()))) {
            addProduct(((ClpProductCreatedEvent) event).getTargetSnapshot());
        }
    }

    private void onUpdatedEvent(ClpEvent event) {
        if ((ProductEventType.PRODUCT_UPDATED.equals(event.getType()))) {
            removeProduct(((ClpProductUpdatedEvent) event).getTargetSnapshot());
            addProduct(((ClpProductUpdatedEvent) event).getTargetSnapshot());
        }
    }

    private void onRemovedEvent(ClpEvent event) {
        if ((ProductEventType.PRODUCT_REMOVED.equals(event.getType()))) {
            removeProduct(((ClpProductRemovedEvent) event).getTargetSnapshot());
        }
    }

    private void addProduct(ProductSnapshot snapshot) {
        products.add(ProfitabilityProductInput.toProduct(snapshot));
    }

    private void removeProduct(ProductSnapshot snapshot) {
        products.removeIf(product -> Objects.equals(product.getProductId(), snapshot.getId()));
    }
}
