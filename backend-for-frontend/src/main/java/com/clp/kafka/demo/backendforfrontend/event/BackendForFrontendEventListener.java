package com.clp.kafka.demo.backendforfrontend.event;

import com.clp.kafka.demo.backendforfrontend.domain.customer.CustomerDomainRepository;
import com.clp.kafka.demo.backendforfrontend.domain.decision.DecisionRepository;
import com.clp.kafka.demo.backendforfrontend.domain.order.PricingOrderDtoRepository;
import com.clp.kafka.demo.backendforfrontend.domain.product.ProductRepository;
import com.clp.kafka.demo.backendforfrontend.domain.profitability.ProfitabilityDtoRepository;
import com.clp.kafka.demo.backendforfrontend.domain.relation.ProductRelationDtoRepository;
import com.clp.kafka.demo.backendforfrontend.domain.standard.ProductStandardDtoRepository;
import com.example.eventcommons.event.customer.CustomerGroupSnapshot;
import com.example.eventcommons.event.customer.event.ClpCustomerGroupPersistedEvent;
import com.example.eventcommons.event.customer.event.ClpCustomerGroupUpdatedEvent;
import com.example.eventcommons.event.customer.event.CustomerEventType;
import com.example.eventcommons.event.decision.event.ClpDecisionChangedEvent;
import com.example.eventcommons.event.decision.event.ClpDecisionPersistedEvent;
import com.example.eventcommons.event.decision.event.ClpFinalDecisionMadeEvent;
import com.example.eventcommons.event.decision.event.DecisionEventType;
import com.example.eventcommons.event.decision.event.DecisionSnapshot;
import com.example.eventcommons.event.global.ClpEvent;
import com.example.eventcommons.event.order.PricingOrderSnapshot;
import com.example.eventcommons.event.order.event.ClpOrderCreatedEvent;
import com.example.eventcommons.event.order.event.ClpOrderPersistedEvent;
import com.example.eventcommons.event.order.event.OrderEventType;
import com.example.eventcommons.event.product.event.ClpProductCreatedEvent;
import com.example.eventcommons.event.product.event.ClpProductModificationCreatedEvent;
import com.example.eventcommons.event.product.event.ClpProductPersistedEvent;
import com.example.eventcommons.event.product.event.ClpProductRemovedEvent;
import com.example.eventcommons.event.product.event.ProductEventType;
import com.example.eventcommons.event.product.event.ClpProductUpdatedEvent;
import com.example.eventcommons.event.product.snapshot.ProductSnapshot;
import com.example.eventcommons.event.productstandard.ClpProductStandardChangedEvent;
import com.example.eventcommons.event.productstandard.ClpProductStandardPersistedEvent;
import com.example.eventcommons.event.productstandard.ProductStandardSnapshot;
import com.example.eventcommons.event.profitability.event.ClpProfitabilityCalculatedEvent;
import com.example.eventcommons.event.profitability.event.ClpProfitabilityPersistedEvent;
import com.example.eventcommons.event.profitability.event.ProfitabilityEventType;
import com.example.eventcommons.event.profitability.event.ProfitabilitySnapshot;
import com.example.eventcommons.event.relations.ClpProductRelationsChangedEvent;
import com.example.eventcommons.event.relations.ClpProductRelationsPersistedEvent;
import com.example.eventcommons.event.relations.ProductRelationSnapshot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

import static org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.METADATA_MAX_AGE_CONFIG;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BackendForFrontendEventListener {
    private final KafkaTemplate<String, ClpEvent> defaultKafkaTemplate;
    private final PricingOrderDtoRepository pricingOrderDtoRepository;
    private final CustomerDomainRepository customerRepository;
    private final ProductRepository productRepository;
    private final ProductRelationDtoRepository productRelationDtoRepository;
    private final ProductStandardDtoRepository productStandardDtoRepository;
    private final ProfitabilityDtoRepository profitabilityDtoRepository;
    private final DecisionRepository decisionRepository;

    private void onCustomerUpdated(ClpCustomerGroupUpdatedEvent event) {
        CustomerGroupSnapshot snapshot = event.getSnapshot();
        customerRepository.upsert(snapshot);
        sendDataPersistedMessage(ClpCustomerGroupPersistedEvent.builder().sagaId(event.getSagaId()).snapshot(snapshot).build());
    }

    private void onOrderCreated(ClpOrderCreatedEvent event) {
        PricingOrderSnapshot snapshot = event.getSnapshot();
        pricingOrderDtoRepository.upsert(snapshot);
        sendDataPersistedMessage(ClpOrderPersistedEvent.builder().sagaId(event.getSagaId()).snapshot(snapshot).build());
    }

    private void onProductCreated(ClpProductCreatedEvent event) {
        ProductSnapshot snapshot = event.getTargetSnapshot();
        productRepository.upsert(snapshot);
        sendDataPersistedMessage(ClpProductPersistedEvent.builder().sagaId(event.getSagaId()).targetSnapshot(snapshot).build());
    }

    private void onProductUpdated(ClpProductUpdatedEvent event) {
        ProductSnapshot snapshot = event.getTargetSnapshot();
        productRepository.upsert(snapshot);
        sendDataPersistedMessage(ClpProductPersistedEvent.builder().sagaId(event.getSagaId()).targetSnapshot(snapshot).build());
    }

    private void onProductRemoved(ClpProductRemovedEvent event) {
        ProductSnapshot snapshot = event.getTargetSnapshot();
        productRepository.deleteById(snapshot.getId());
        productStandardDtoRepository.deleteById(snapshot.getId());
    }

    private void onProductModified(ClpProductModificationCreatedEvent event) {
        ProductSnapshot targetSnapshot = event.getTargetSnapshot();
        ProductSnapshot sourceSnapshot = event.getSourceSnapshot();
        productRepository.upsert(targetSnapshot);
        productRepository.upsert(sourceSnapshot);
        sendDataPersistedMessage(ClpProductPersistedEvent.builder().sagaId(event.getSagaId()).targetSnapshot(targetSnapshot).build());
    }

    private void onProductRelationsChanged(ClpProductRelationsChangedEvent event) {
        Set<ProductRelationSnapshot> productRelationSnapshots = event.getProductRelationSnapshots();
        productRelationDtoRepository.saveAll(productRelationSnapshots);
        sendDataPersistedMessage(ClpProductRelationsPersistedEvent.builder().sagaId(event.getSagaId()).pricingOrderId(event.getOrderId()).productRelationSnapshots(productRelationSnapshots).build());
    }

    private void onProductStandardChanged(ClpProductStandardChangedEvent event) {
        Set<ProductStandardSnapshot> snapshots = event.getProductStandardSnapshots();
        productStandardDtoRepository.saveAll(snapshots);
        sendDataPersistedMessage(ClpProductStandardPersistedEvent.builder().sagaId(event.getSagaId()).orderId(event.getOrderId()).productStandardSnapshots(snapshots).build());
    }

    private void onProfitabilityCalculated(ClpProfitabilityCalculatedEvent event) {
        ProfitabilitySnapshot snapshot = event.getSnapshot();
        profitabilityDtoRepository.upsert(snapshot);
        sendDataPersistedMessage(ClpProfitabilityPersistedEvent.builder().sagaId(event.getSagaId()).orderId(event.getOrderId()).snapshot(snapshot).build());
    }

    private void onDecisionChanged(ClpDecisionChangedEvent event) {
        DecisionSnapshot decisionSnapshot = event.getSnapshot();
        decisionRepository.upsert(decisionSnapshot);
        sendDataPersistedMessage(ClpDecisionPersistedEvent.builder().sagaId(event.getSagaId()).snapshot(decisionSnapshot).build());
    }


    private void onFinalDecisionMade(ClpFinalDecisionMadeEvent event) {
        DecisionSnapshot decisionSnapshot = event.getSnapshot();
        decisionRepository.archive(decisionSnapshot);
        sendDataPersistedMessage(ClpDecisionPersistedEvent.builder().sagaId(event.getSagaId()).snapshot(decisionSnapshot).build());
    }

    private void sendDataPersistedMessage(ClpEvent event) {
        defaultKafkaTemplate.send(new ProducerRecord<>(event.getDefaultTopic(), event.getDefaultKey(), event));
    }

    @KafkaListener(topicPattern = "CLP_ORDER_.*", properties = {METADATA_MAX_AGE_CONFIG + ":1000", AUTO_OFFSET_RESET_CONFIG + ":earliest"})
    public void listenToOrdersRecords(ConsumerRecord<String, ClpEvent> record) {
        log.info("BackendForFrontendEventListener : {} {}", record.key(), record);
        ClpEvent event = record.value();
        String eventType = event.getType().name();

        if (OrderEventType.ORDER_CREATED.name().equals(eventType)) {
            onOrderCreated((ClpOrderCreatedEvent) event);
        }
        if (ProductEventType.PRODUCT_CREATED.name().equals(eventType)) {
            onProductCreated((ClpProductCreatedEvent) event);
        }
        if (ProductEventType.PRODUCT_UPDATED.name().equals(eventType)) {
            onProductUpdated((ClpProductUpdatedEvent) event);
        }
        if (ProductEventType.PRODUCT_REMOVED.name().equals(eventType)) {
            onProductRemoved((ClpProductRemovedEvent) event);
        }
        if (ProductEventType.PRODUCT_MODIFIED.name().equals(eventType)) {
            onProductModified((ClpProductModificationCreatedEvent) event);
        }
        if (ProductEventType.PRODUCT_RELATIONS_CHANGED.name().equals(eventType)) {
            onProductRelationsChanged((ClpProductRelationsChangedEvent) event);
        }
        if (ProductEventType.PRODUCT_STANDARD_CHANGED.name().equals(eventType)) {
            onProductStandardChanged((ClpProductStandardChangedEvent) event);
        }
        if (ProfitabilityEventType.PROFITABILITY_CALCULATED.name().equals(eventType)) {
            onProfitabilityCalculated((ClpProfitabilityCalculatedEvent) event);
        }
        if (DecisionEventType.DECISION_CHANGED.name().equals(eventType)) {
            onDecisionChanged((ClpDecisionChangedEvent) event);
        }
        if (DecisionEventType.FINAL_DECISION_MADE.name().equals(eventType)) {
            onFinalDecisionMade((ClpFinalDecisionMadeEvent) event);
        }
    }

    @KafkaListener(topicPattern = "CLP_CUSTOMERS_CHANGES", properties = {AUTO_OFFSET_RESET_CONFIG + ":earliest"})
    public void listenToCustomerChanges(ConsumerRecord<String, ClpEvent> record) {
        log.info("Backend For Frontend EventListener : {} {}", record.key(), record);
        ClpEvent event = record.value();
        String eventType = event.getType().name();


        if (CustomerEventType.CUSTOMER_UPDATED.name().equals(eventType)) {
            onCustomerUpdated((ClpCustomerGroupUpdatedEvent) event);
        }
    }
}
