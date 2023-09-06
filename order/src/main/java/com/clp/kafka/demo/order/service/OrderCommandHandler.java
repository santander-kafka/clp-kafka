package com.clp.kafka.demo.order.service;

import com.clp.kafka.demo.order.domain.OrderRepository;
import com.clp.kafka.demo.order.event.OrderEventHandler;
import com.example.eventcommons.event.order.PricingOrderSnapshot;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicListing;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.Uuid;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class OrderCommandHandler {
    private final OrderRepository repository;
    private final OrderEventHandler kafkaHandler;
    private final AdminClient adminClient;

    @Transactional
    public UUID handleCreateOrderRequest(String groupId) {
        UUID sagaId = kafkaHandler.startCreateOrderCreationSaga();
        PricingOrderSnapshot orderSnapshot = repository.createOrder(groupId);
        creteTopic(orderSnapshot);
        kafkaHandler.sendOrderCreatedMessage(orderSnapshot, sagaId);
        return sagaId;
    }

    private void creteTopic(PricingOrderSnapshot orderSnapshot) {
        String topicName = getTopicName(orderSnapshot.getId());
        if (!isCreated(topicName)) {
            createOrderTopic(orderSnapshot.getId());
            waitForTopicCreated(getTopicName(orderSnapshot.getId()));
        }
    }

    public void waitForTopicCreated(String topicName) {
        int i = 0;
        boolean isCreated = false;
        while (i < 20 && !isCreated) {
            log.info("i: {}", i);
            isCreated = isCreated(topicName);
            i++;
        }

        if (!isCreated) {
            throw new RuntimeException("Topic NOT created: " + topicName);
        }
    }

    @SneakyThrows
    public boolean isCreated(String topicName) {
        KafkaFuture<Collection<TopicListing>> listingsFuture = adminClient.listTopics().listings();

        while (!listingsFuture.isDone()) {
            Thread.sleep(200);
        }

        log.info("Is topic created - name: {}, list: {}", topicName, listingsFuture.get().stream().map(TopicListing::name).collect(Collectors.toList()));
        return listingsFuture.get().stream().anyMatch(topic -> Objects.equals(topic.name(), topicName));
    }

    private void createOrderTopic(Long orderId) {
        NewTopic orderTopic = TopicBuilder.name(getTopicName(orderId))
                .partitions(3)
                .replicas(2)
                .config(TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG, "1")
                .compact()
                .build();
        adminClient.createTopics(List.of(orderTopic))
                .topicId(orderTopic.name());
    }

    private String getTopicName(Long orderId) {
        return "CLP_ORDER_" + orderId.toString();
    }
}
