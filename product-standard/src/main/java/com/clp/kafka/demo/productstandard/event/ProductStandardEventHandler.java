package com.clp.kafka.demo.productstandard.event;

import com.clp.kafka.demo.productstandard.domain.input.DefaultProductStandardInput;
import com.clp.kafka.demo.productstandard.domain.model.ProductStandardCalculator;
import com.example.eventcommons.event.global.ClpEvent;
import com.example.eventcommons.event.product.event.ClpProductCreatedEvent;
import com.example.eventcommons.event.product.event.ClpProductModificationCreatedEvent;
import com.example.eventcommons.event.product.event.ClpProductRenealCreatedEvent;
import com.example.eventcommons.event.product.event.ClpProductResignationCreatedEvent;
import com.example.eventcommons.event.product.event.ProductEventType;
import com.example.eventcommons.event.product.event.ClpProductUpdatedEvent;
import com.example.eventcommons.event.product.snapshot.ProductSnapshot;
import com.example.eventcommons.event.productstandard.ClpProductStandardChangedEvent;
import com.example.eventcommons.event.productstandard.ProductStandardSnapshot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

import static org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.METADATA_MAX_AGE_CONFIG;


@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductStandardEventHandler {
    private final ProductStandardCalculator calculator;
    private final KafkaTemplate<String, ClpEvent> defaultKafkaTemplate;

    @KafkaListener(topicPattern = "CLP_ORDER_.*", properties = {METADATA_MAX_AGE_CONFIG + ":1000", AUTO_OFFSET_RESET_CONFIG + ":earliest"})
    public void listenToOrdersRecords(ConsumerRecord<String, ClpEvent> record) {
        log.info("ProductRelationEventHandler : {} {}", record.key(), record);
        ClpEvent event = record.value();
        String eventType = event.getType().name();

        if (ProductEventType.PRODUCT_CREATED.name().equals(eventType)) {
//            throw new IllegalArgumentException();
            onProductCreated((ClpProductCreatedEvent) event);
        }
        if (ProductEventType.PRODUCT_UPDATED.name().equals(eventType)) {
            onProductUpdated((ClpProductUpdatedEvent) event);
        }
        if (ProductEventType.PRODUCT_MODIFIED.name().equals(eventType)) {
            onProductModificationCreated((ClpProductModificationCreatedEvent) event);
        }
        if (ProductEventType.PRODUCT_RENEWED.name().equals(eventType)) {
            onProductRenewalCreated((ClpProductRenealCreatedEvent) event);
        }
        if (ProductEventType.PRODUCT_RESIGNED.name().equals(eventType)) {
            onProductResignationCreated((ClpProductResignationCreatedEvent) event);
        }
    }

    private void onProductCreated(ClpProductCreatedEvent event) {
        ProductSnapshot snapshot = event.getTargetSnapshot();
        Set<ProductStandardSnapshot> standardSnapshot = calculator.calculateStandard(DefaultProductStandardInput.from(snapshot));
        sendStandardUpdatedMessage(event.getSagaId(), event.getTargetSnapshot().getOrderId(), standardSnapshot);
    }

    private void onProductUpdated(ClpProductUpdatedEvent event) {
        ProductSnapshot snapshot = event.getTargetSnapshot();
        Set<ProductStandardSnapshot> standardSnapshot = calculator.calculateStandard(DefaultProductStandardInput.from(snapshot));
        sendStandardUpdatedMessage(event.getSagaId(), event.getTargetSnapshot().getOrderId(), standardSnapshot);
    }

    private void onProductModificationCreated(ClpProductModificationCreatedEvent event) {
        ProductSnapshot targetSnapshot = event.getTargetSnapshot();
        ProductSnapshot sourceSnapshot = event.getSourceSnapshot();
        Set<ProductStandardSnapshot> standardSnapshot = calculator.calculateStandard(DefaultProductStandardInput.from(targetSnapshot, sourceSnapshot));
        sendStandardUpdatedMessage(event.getSagaId(), event.getTargetSnapshot().getOrderId(), standardSnapshot);
    }

    private void onProductRenewalCreated(ClpProductRenealCreatedEvent event) {
        ProductSnapshot targetSnapshot = event.getTargetSnapshot();
        ProductSnapshot sourceSnapshot = event.getSourceSnapshot();
        Set<ProductStandardSnapshot> standardSnapshot = calculator.calculateStandard(DefaultProductStandardInput.from(targetSnapshot, sourceSnapshot));
        sendStandardUpdatedMessage(event.getSagaId(), event.getTargetSnapshot().getOrderId(), standardSnapshot);
    }

    private void onProductResignationCreated(ClpProductResignationCreatedEvent event) {
        ProductSnapshot targetSnapshot = event.getTargetSnapshot();
        ProductSnapshot sourceSnapshot = event.getSourceSnapshot();
        Set<ProductStandardSnapshot> standardSnapshot = calculator.calculateStandard(DefaultProductStandardInput.from(targetSnapshot, sourceSnapshot));
        sendStandardUpdatedMessage(event.getSagaId(), event.getTargetSnapshot().getOrderId(), standardSnapshot);
    }

    private void sendStandardUpdatedMessage(UUID sagaId, Long orderId, Set<ProductStandardSnapshot> productStandardSnapshots) {
        ClpProductStandardChangedEvent event = ClpProductStandardChangedEvent.builder()
                .sagaId(sagaId)
                .pricingOrderId(orderId)
                .productStandardSnapshots(productStandardSnapshots)
                .build();

        sendDataUpdatedMessage(event);
    }

    private void sendDataUpdatedMessage(ClpEvent event) {
        defaultKafkaTemplate.send(new ProducerRecord<>(event.getDefaultTopic(), event.getDefaultKey(), event));
    }
}
