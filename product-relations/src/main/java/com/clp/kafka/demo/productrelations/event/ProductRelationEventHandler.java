package com.clp.kafka.demo.productrelations.event;

import com.clp.kafka.demo.productrelations.domain.input.DefaultProductRelationsInput;
import com.clp.kafka.demo.productrelations.event.port.ProductRelationRepositoryPort;
import com.example.eventcommons.event.global.ClpEvent;
import com.example.eventcommons.event.product.event.ClpProductCanceledEvent;
import com.example.eventcommons.event.product.event.ClpProductCreatedEvent;
import com.example.eventcommons.event.product.event.ClpProductModificationCreatedEvent;
import com.example.eventcommons.event.product.event.ClpProductRejectedEvent;
import com.example.eventcommons.event.product.event.ClpProductRemovedEvent;
import com.example.eventcommons.event.product.event.ClpProductRenealCreatedEvent;
import com.example.eventcommons.event.product.event.ClpProductResignationCreatedEvent;
import com.example.eventcommons.event.product.event.ProductEventType;
import com.example.eventcommons.event.product.event.ClpProductUpdatedEvent;
import com.example.eventcommons.event.product.snapshot.ProductSnapshot;
import com.example.eventcommons.event.relations.ClpProductRelationsChangedEvent;
import com.example.eventcommons.event.relations.ProductRelationSnapshot;
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
public class ProductRelationEventHandler {
    private final ProductRelationRepositoryPort productRelationRepository;
    private final KafkaTemplate<String, ClpEvent> defaultKafkaTemplate;

    private void onProductCreated(ClpProductCreatedEvent event) {
        ProductSnapshot snapshot = event.getTargetSnapshot();
        Set<ProductRelationSnapshot> productRelationSnapshots = productRelationRepository.onProductCreated(DefaultProductRelationsInput.from(snapshot));
        sendRelationsChangedMessage(event.getSagaId(), event.getTargetSnapshot().getOrderId(), productRelationSnapshots);
    }

    private void onProductUpdated(ClpProductUpdatedEvent event) {
        ProductSnapshot snapshot = event.getTargetSnapshot();
        Set<ProductRelationSnapshot> productRelationSnapshots = productRelationRepository.onProductUpdated(DefaultProductRelationsInput.from(snapshot));
        sendRelationsChangedMessage(event.getSagaId(), event.getTargetSnapshot().getOrderId(), productRelationSnapshots);
    }

    private void onProductModificationCreated(ClpProductModificationCreatedEvent event) {
        ProductSnapshot targetSnapshot = event.getTargetSnapshot();
        ProductSnapshot sourceSnapshot = event.getSourceSnapshot();
        Set<ProductRelationSnapshot> productRelationSnapshots = productRelationRepository.onModificationCreated(DefaultProductRelationsInput.from(targetSnapshot, sourceSnapshot));
        sendRelationsChangedMessage(event.getSagaId(), event.getTargetSnapshot().getOrderId(), productRelationSnapshots);
    }

    private void onProductRenewalCreated(ClpProductRenealCreatedEvent event) {
        ProductSnapshot targetSnapshot = event.getTargetSnapshot();
        ProductSnapshot sourceSnapshot = event.getSourceSnapshot();
        Set<ProductRelationSnapshot> productRelationSnapshots = productRelationRepository.onRenewalCreated(DefaultProductRelationsInput.from(targetSnapshot, sourceSnapshot));
        sendRelationsChangedMessage(event.getSagaId(), event.getTargetSnapshot().getOrderId(), productRelationSnapshots);
    }

    private void onProductResignationCreated(ClpProductResignationCreatedEvent event) {
        ProductSnapshot targetSnapshot = event.getTargetSnapshot();
        ProductSnapshot sourceSnapshot = event.getSourceSnapshot();
        Set<ProductRelationSnapshot> productRelationSnapshots = productRelationRepository.onResignationCreated(DefaultProductRelationsInput.from(targetSnapshot, sourceSnapshot));
        sendRelationsChangedMessage(event.getSagaId(), event.getTargetSnapshot().getOrderId(), productRelationSnapshots);
    }

    private void onProductCanceled(ClpProductCanceledEvent event) {
        ProductSnapshot targetSnapshot = event.getTargetSnapshot();
        Set<ProductRelationSnapshot> productRelationSnapshots = productRelationRepository.onProductCanceled(DefaultProductRelationsInput.from(targetSnapshot));
        sendRelationsChangedMessage(event.getSagaId(), event.getTargetSnapshot().getOrderId(), productRelationSnapshots);
    }

    private void onProductRejected(ClpProductRejectedEvent event) {
        ProductSnapshot targetSnapshot = event.getTargetSnapshot();
        Set<ProductRelationSnapshot> productRelationSnapshots = productRelationRepository.onProductRejected(DefaultProductRelationsInput.from(targetSnapshot));
        sendRelationsChangedMessage(event.getSagaId(), event.getTargetSnapshot().getOrderId(), productRelationSnapshots);
    }

    private void onProductRemoved(ClpProductRemovedEvent event) {
        ProductSnapshot targetSnapshot = event.getTargetSnapshot();
        Set<ProductRelationSnapshot> productRelationSnapshots = productRelationRepository.onProductRemoved(DefaultProductRelationsInput.from(targetSnapshot));
        sendRelationsChangedMessage(event.getSagaId(), event.getTargetSnapshot().getOrderId(), productRelationSnapshots);
    }

    @KafkaListener(topicPattern = "CLP_ORDER_.*", properties = {METADATA_MAX_AGE_CONFIG + ":1000", AUTO_OFFSET_RESET_CONFIG + ":earliest"})
    public void listenToOrdersRecords(ConsumerRecord<String, ClpEvent> record) {
        log.info("ProductRelationEventHandler : {} {}", record.key(), record);
        ClpEvent event = record.value();
        String eventType = event.getType().name();

        if (ProductEventType.PRODUCT_CREATED.name().equals(eventType)) {
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
        if (ProductEventType.PRODUCT_CANCELED.name().equals(eventType)) {
            onProductCanceled((ClpProductCanceledEvent) event);
        }
        if (ProductEventType.PRODUCT_REJECTED.name().equals(eventType)) {
            onProductRejected((ClpProductRejectedEvent) event);
        }
        if (ProductEventType.PRODUCT_REMOVED.name().equals(eventType)) {
            onProductRemoved((ClpProductRemovedEvent) event);
        }
    }

    private void sendRelationsChangedMessage(UUID sagaId, Long orderId, Set<ProductRelationSnapshot> productRelationSnapshots) {
        ClpProductRelationsChangedEvent event = ClpProductRelationsChangedEvent.builder()
                .sagaId(sagaId)
                .pricingOrderId(orderId)
                .productRelationSnapshots(productRelationSnapshots)
                .build();

        sendDataUpdatedMessage(event);
    }

    private void sendDataUpdatedMessage(ClpEvent event) {
        defaultKafkaTemplate.send(new ProducerRecord<>(event.getDefaultTopic(), event.getDefaultKey(), event));
    }
}
