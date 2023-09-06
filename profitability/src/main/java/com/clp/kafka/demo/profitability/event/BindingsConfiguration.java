package com.clp.kafka.demo.profitability.event;

import com.clp.kafka.demo.profitability.service.ProfitabilityService;
import com.example.eventcommons.event.global.ClpEvent;
import com.example.eventcommons.event.global.ClpOrderRelatedEvent;
import com.example.eventcommons.event.order.event.OrderEventType;
import com.example.eventcommons.event.product.event.ProductEventType;
import com.example.eventcommons.event.profitability.event.ClpProfitabilityCalculatedEvent;
import com.example.eventcommons.event.profitability.event.ProfitabilitySnapshot;
import com.example.eventcommons.event.serde.DefaultEventSerde;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Predicate;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueBytesStoreSupplier;
import org.apache.kafka.streams.state.Stores;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.util.function.Consumer;
import java.util.stream.Stream;

@Configuration
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BindingsConfiguration {
    private final ProfitabilityService profitabilityService;

    public static final String PROFITABILITY_INPUT_STREAM_STORE = "latest-profitability-store";


    @Bean
    public Consumer<KStream<String, ClpEvent>> aggregate() {
        KeyValueBytesStoreSupplier storeSupplier = Stores.persistentKeyValueStore(PROFITABILITY_INPUT_STREAM_STORE);
        return orderEvents -> aggregateInput(orderEvents, storeSupplier);
    }

    private void aggregateInput(KStream<String, ClpEvent> orderEvents, KeyValueBytesStoreSupplier storeSupplier) {
        orderEvents
                .filter(byProfitabilityRelevantRelevantEvents())
                .groupBy((key, event) -> ((ClpOrderRelatedEvent) event).getOrderId().toString(),
                        Grouped.with(Serdes.String(), new DefaultEventSerde()))
                .aggregate(
                        ProfitabilityEvents::new,
                        (orderId, event, result) -> result.onEvent(event),
                        Materialized.<String, ProfitabilityEvents>as(storeSupplier)
                                .withKeySerde(Serdes.String())
                                .withValueSerde(new JsonSerde<>(ProfitabilityEvents.class)))
                .toStream()
                .foreach(profitabilityService::updateInput);

        invalidatePersistedInput(orderEvents);
    }

    private void invalidatePersistedInput(KStream<String, ClpEvent> profitabilityEvents) {
        profitabilityEvents
                .filter(byProfitabilityRelevantRelevantEvents())
                .mapValues((key, event) -> {
                    ClpOrderRelatedEvent profitabilityEvent = (ClpOrderRelatedEvent) event;
                    return ClpProfitabilityCalculatedEvent.builder()
                            .snapshot(ProfitabilitySnapshot.builder()
                                    .orderId(profitabilityEvent.getOrderId())
                                    .build())
                            .sagaId(profitabilityEvent.getSagaId())
                            .build();
                }).to(
                        (key, event, context) -> context.topic(),
                        Produced.with(Serdes.String(), new JsonSerde<>(ClpProfitabilityCalculatedEvent.class))
                );
    }

    private Predicate<String, ClpEvent> byProfitabilityRelevantRelevantEvents() {
        return (key, event) -> Stream.of(
                OrderEventType.ORDER_CREATED,
                ProductEventType.PRODUCT_CREATED,
                ProductEventType.PRODUCT_UPDATED,
                ProductEventType.PRODUCT_REMOVED
        ).anyMatch(type -> type.name().equals(event.getType().name()));
    }
}
