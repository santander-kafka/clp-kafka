package com.clp.kafka.demo.product.event;

import com.clp.kafka.demo.product.event.saga.repository.DefaultProductSagaRepository;
import com.example.eventcommons.event.decision.event.DecisionEventType;
import com.example.eventcommons.event.global.ClpEvent;
import com.example.eventcommons.event.product.event.ClpProductCreatedEvent;
import com.example.eventcommons.event.product.event.ClpProductRemovedEvent;
import com.example.eventcommons.event.product.event.ProductEventType;
import com.example.eventcommons.event.product.event.ClpProductUpdatedEvent;
import com.example.eventcommons.event.product.snapshot.ProductSnapshot;
import com.example.eventcommons.event.profitability.event.ProfitabilityEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.METADATA_MAX_AGE_CONFIG;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class ProductEventHandler {
    private final KafkaTemplate<String, ClpEvent> defaultKafkaTemplate;
    private final DefaultProductSagaRepository defaultProductSagaRepository;

    public UUID handleProductCreated(ProductSnapshot snapshot) {
        UUID sagaId = defaultProductSagaRepository.start();
        sendProductCreatedMessage(snapshot, sagaId);
        return sagaId;
    }

    public UUID handleProductUpdated(ProductSnapshot snapshot) {
        UUID sagaId = defaultProductSagaRepository.start();
        sendProductUpdatedMessage(snapshot, sagaId);
        return sagaId;
    }


    public UUID handleProductRemoved(ProductSnapshot snapshot) {
        UUID sagaId = UUID.randomUUID();
        // todo saga
        sendProductRemovedMessage(snapshot, sagaId);
        return sagaId;
    }

    private void sendProductCreatedMessage(ProductSnapshot snapshot, UUID sagaId) {
        ClpProductCreatedEvent event = ClpProductCreatedEvent.builder()
                .sagaId(sagaId)
                .targetSnapshot(snapshot)
                .build();
        sendMessage(event);
    }

    private void sendProductUpdatedMessage(ProductSnapshot snapshot, UUID sagaId) {
        ClpProductUpdatedEvent event = ClpProductUpdatedEvent.builder()
                .sagaId(sagaId)
                .targetSnapshot(snapshot)
                .build();
        sendMessage(event);
    }

    private void sendProductRemovedMessage(ProductSnapshot snapshot, UUID sagaId) {
        ClpProductRemovedEvent event = ClpProductRemovedEvent.builder()
                .sagaId(sagaId)
                .targetSnapshot(snapshot)
                .build();
        sendMessage(event);
    }

    private void sendMessage(ClpEvent event) {
        defaultKafkaTemplate.send(new ProducerRecord<>(event.getDefaultTopic(), event.getDefaultKey(), event));
    }

    @KafkaListener(topicPattern = "CLP_ORDER_.*", properties = { METADATA_MAX_AGE_CONFIG + ":1000", AUTO_OFFSET_RESET_CONFIG + ":earliest" })
    private void listenToOrdersRecords(ConsumerRecord<String, ClpEvent> record) {
        log.info("ProductEventHandler received : {}", record.toString());
        ClpEvent event = record.value();
        String eventType = event.getType().name();

        if (ProductEventType.PRODUCT_PERSISTED.name().equals(eventType)) {
            defaultProductSagaRepository.successProductPersisted(event.getSagaId());
        }

        if (ProductEventType.PRODUCT_RELATIONS_PERSISTED.name().equals(eventType)) {
            defaultProductSagaRepository.successProductRelationsPersisted(event.getSagaId());
        }

        if (ProductEventType.PRODUCT_STANDARD_PERSISTED.name().equals(eventType)) {
            defaultProductSagaRepository.successProductStandardPersisted(event.getSagaId());
        }

        if (ProfitabilityEventType.PROFITABILITY_PERSISTED.name().equals(eventType)) {
            defaultProductSagaRepository.profitabilityInputPersisted(event.getSagaId());
        }

        if (DecisionEventType.DECISION_PERSISTED.name().equals(eventType)) {
            defaultProductSagaRepository.decisionUpdated(event.getSagaId());
        }
    }
}

