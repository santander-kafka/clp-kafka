package com.clp.kafka.demo.order.event;

import com.clp.kafka.demo.order.domain.OrderRepository;
import com.clp.kafka.demo.order.event.saga.create.OrderCreationSagaRepository;
import com.example.eventcommons.event.decision.event.DecisionEventType;
import com.example.eventcommons.event.global.ClpEvent;
import com.example.eventcommons.event.order.PricingOrderSnapshot;
import com.example.eventcommons.event.order.event.ClpOrderCreatedEvent;
import com.example.eventcommons.event.order.event.OrderEventType;
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
public class OrderEventHandler {
    private final KafkaTemplate<String, ClpEvent> defaultKafkaTemplate;
    private final OrderCreationSagaRepository orderCreationSagaRepository;
    private final OrderRepository orderRepository;

    public UUID startCreateOrderCreationSaga() {
        return orderCreationSagaRepository.startSaga();
    }

    public void sendOrderCreatedMessage(PricingOrderSnapshot snapshot, UUID sagaId) {
        ClpOrderCreatedEvent event = ClpOrderCreatedEvent.builder().sagaId(sagaId).snapshot(snapshot).build();
        defaultKafkaTemplate.send(new ProducerRecord<>(event.getDefaultTopic(), event.getDefaultKey(), event));
    }

    @KafkaListener(topicPattern = "CLP_ORDER_.*", properties = {METADATA_MAX_AGE_CONFIG + ":1000", AUTO_OFFSET_RESET_CONFIG + ":earliest"})
    private void listenToOrdersRecords(ConsumerRecord<String, ClpEvent> record) {
        log.info("OrderEventHandler received : {}", record.toString());
        ClpEvent event = record.value();
        String eventType = event.getType().name();

        if (OrderEventType.ORDER_CREATED.name().equals(eventType)) {
            orderCreationSagaRepository.successOrderCreated(event.getSagaId());
        }

        if (OrderEventType.ORDER_PERSISTED.name().equals(eventType)) {
            orderCreationSagaRepository.successOrderPersisted(event.getSagaId());
        }
    }

}

