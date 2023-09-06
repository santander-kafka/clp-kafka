package com.clp.kafka.demo.decision.event;

import com.clp.kafka.demo.decision.domain.DecisionApproverRepository;
import com.example.eventcommons.event.decision.event.ClpDecisionChangedEvent;
import com.example.eventcommons.event.decision.event.ClpFinalDecisionMadeEvent;
import com.example.eventcommons.event.decision.event.DecisionSnapshot;
import com.example.eventcommons.event.global.ClpEvent;
import com.example.eventcommons.event.order.event.ClpOrderCreatedEvent;
import com.example.eventcommons.event.order.event.OrderEventType;
import com.example.eventcommons.event.product.event.ClpProductCreatedEvent;
import com.example.eventcommons.event.product.event.ClpProductModificationCreatedEvent;
import com.example.eventcommons.event.product.event.ClpProductRemovedEvent;
import com.example.eventcommons.event.product.event.ProductEventType;
import com.example.eventcommons.event.product.event.ClpProductUpdatedEvent;
import com.example.eventcommons.event.productstandard.ClpProductStandardChangedEvent;
import com.example.eventcommons.event.profitability.event.ClpProfitabilityCalculatedEvent;
import com.example.eventcommons.event.profitability.event.ProfitabilityEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.METADATA_MAX_AGE_CONFIG;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DecisionEventsHandler {
    private final KafkaTemplate<String, ClpEvent> defaultKafkaTemplate;
    private final DecisionApproverRepository repository;

    @KafkaListener(topicPattern = "CLP_ORDER_.*", properties = {METADATA_MAX_AGE_CONFIG + ":1000", AUTO_OFFSET_RESET_CONFIG + ":earliest"})
    @Transactional
    public void listenToOrdersRecords(ConsumerRecord<String, ClpEvent> record) {
        log.info("BackendForFrontendEventListener : {} {}", record.key(), record);
        ClpEvent event = record.value();
        String eventType = event.getType().name();

        if (OrderEventType.ORDER_CREATED.name().equals(eventType)) {
            DecisionSnapshot decisionSnapshot = repository.onOrderCreated((ClpOrderCreatedEvent) event);
            sendDecisionChangedMessageEvent(decisionSnapshot, event.getSagaId());
        }
        if (ProductEventType.PRODUCT_CREATED.name().equals(eventType)) {
            DecisionSnapshot decisionSnapshot = repository.onProductCreated((ClpProductCreatedEvent) event);
            sendDecisionChangedMessageEvent(decisionSnapshot, event.getSagaId());
        }
        if (ProductEventType.PRODUCT_UPDATED.name().equals(eventType)) {
            DecisionSnapshot decisionSnapshot = repository.onProductUpdated((ClpProductUpdatedEvent) event);
            sendDecisionChangedMessageEvent(decisionSnapshot, event.getSagaId());
        }
        if (ProductEventType.PRODUCT_REMOVED.name().equals(eventType)) {
            DecisionSnapshot decisionSnapshot = repository.onProductRemoved((ClpProductRemovedEvent) event);
            sendDecisionChangedMessageEvent(decisionSnapshot, event.getSagaId());
        }
        if (ProductEventType.PRODUCT_MODIFIED.name().equals(eventType)) {
            DecisionSnapshot decisionSnapshot = repository.onProductModified((ClpProductModificationCreatedEvent) event);
            sendDecisionChangedMessageEvent(decisionSnapshot, event.getSagaId());
        }
        if (ProductEventType.PRODUCT_STANDARD_CHANGED.name().equals(eventType)) {
            DecisionSnapshot decisionSnapshot = repository.onProductStandardChanged((ClpProductStandardChangedEvent) event);
            sendDecisionChangedMessageEvent(decisionSnapshot, event.getSagaId());
        }
        if (ProfitabilityEventType.PROFITABILITY_CALCULATED.name().equals(eventType)) {
            DecisionSnapshot decisionSnapshot = repository.onProfitabilityCalculated((ClpProfitabilityCalculatedEvent) event);
            sendDecisionChangedMessageEvent(decisionSnapshot, event.getSagaId());
        }
    }

    public UUID sendDecisionChangedMessageEvent(DecisionSnapshot snapshot, UUID sagaId) {
        ClpDecisionChangedEvent event = ClpDecisionChangedEvent.builder()
                .sagaId(sagaId)
                .snapshot(snapshot)
                .build();
        defaultKafkaTemplate.send(new ProducerRecord<>(event.getDefaultTopic(), event.getDefaultKey(), event));
        return event.getSagaId();
    }

    public UUID sendFinalDecisionMessageEvent(DecisionSnapshot decisionSnapshot, UUID sagaId) {
        ClpFinalDecisionMadeEvent event = ClpFinalDecisionMadeEvent.builder()
                .sagaId(sagaId)
                .snapshot(decisionSnapshot)
                .build();
        defaultKafkaTemplate.send(new ProducerRecord<>(event.getDefaultTopic(), event.getDefaultKey(), event));
        return event.getSagaId();
    }
}
