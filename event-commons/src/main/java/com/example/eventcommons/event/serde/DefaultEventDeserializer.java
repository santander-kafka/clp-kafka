package com.example.eventcommons.event.serde;

import com.example.eventcommons.event.customer.event.ClpCustomerGroupPersistedEvent;
import com.example.eventcommons.event.customer.event.ClpCustomerGroupUpdatedEvent;
import com.example.eventcommons.event.customer.event.CustomerEventType;
import com.example.eventcommons.event.decision.event.ClpDecisionChangedEvent;
import com.example.eventcommons.event.decision.event.ClpDecisionPersistedEvent;
import com.example.eventcommons.event.decision.event.ClpFinalDecisionMadeEvent;
import com.example.eventcommons.event.decision.event.DecisionEventType;
import com.example.eventcommons.event.global.ClpEvent;
import com.example.eventcommons.event.order.event.ClpOrderCreatedEvent;
import com.example.eventcommons.event.order.event.ClpOrderPersistedEvent;
import com.example.eventcommons.event.order.event.OrderEventType;
import com.example.eventcommons.event.product.event.ClpProductCreatedEvent;
import com.example.eventcommons.event.product.event.ClpProductModificationCreatedEvent;
import com.example.eventcommons.event.product.event.ClpProductPersistedEvent;
import com.example.eventcommons.event.product.event.ClpProductRemovedEvent;
import com.example.eventcommons.event.product.event.ProductEventType;
import com.example.eventcommons.event.product.event.ClpProductUpdatedEvent;
import com.example.eventcommons.event.productstandard.ClpProductStandardChangedEvent;
import com.example.eventcommons.event.productstandard.ClpProductStandardPersistedEvent;
import com.example.eventcommons.event.profitability.event.ClpProfitabilityCalculatedEvent;
import com.example.eventcommons.event.profitability.event.ClpProfitabilityInputPersistedChangedEvent;
import com.example.eventcommons.event.profitability.event.ClpProfitabilityPersistedEvent;
import com.example.eventcommons.event.profitability.event.ProfitabilityEventType;
import com.example.eventcommons.event.relations.ClpProductRelationsChangedEvent;
import com.example.eventcommons.event.relations.ClpProductRelationsPersistedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;

public class DefaultEventDeserializer implements Deserializer<ClpEvent> {
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @Override
    public ClpEvent deserialize(String topic, byte[] data) {
        return deserialize(data);
    }

    @Override
    public ClpEvent deserialize(String topic, Headers headers, byte[] data) {
        return deserialize(data);
    }

    private ClpEvent deserialize(byte[] data) {
        String eventType = eventTypeFromByteArray(data);

        if (OrderEventType.ORDER_CREATED.name().equals(eventType)) {
            return fromByteArray(data, ClpOrderCreatedEvent.class);
        }
        if (OrderEventType.ORDER_PERSISTED.name().equals(eventType)) {
            return fromByteArray(data, ClpOrderPersistedEvent.class);
        }

        if (ProductEventType.PRODUCT_CREATED.name().equals(eventType)) {
            return fromByteArray(data, ClpProductCreatedEvent.class);
        }
        if (ProductEventType.PRODUCT_UPDATED.name().equals(eventType)) {
            return fromByteArray(data, ClpProductUpdatedEvent.class);
        }
        if (ProductEventType.PRODUCT_MODIFIED.name().equals(eventType)) {
            return fromByteArray(data, ClpProductModificationCreatedEvent.class);
        }
        if (ProductEventType.PRODUCT_PERSISTED.name().equals(eventType)) {
            return fromByteArray(data, ClpProductPersistedEvent.class);
        }

        if (ProductEventType.PRODUCT_RELATIONS_CHANGED.name().equals(eventType)) {
            return fromByteArray(data, ClpProductRelationsChangedEvent.class);
        }
        if (ProductEventType.PRODUCT_RELATIONS_PERSISTED.name().equals(eventType)) {
            return fromByteArray(data, ClpProductRelationsPersistedEvent.class);
        }

        if (ProductEventType.PRODUCT_STANDARD_CHANGED.name().equals(eventType)) {
            return fromByteArray(data, ClpProductStandardChangedEvent.class);
        }
        if (ProductEventType.PRODUCT_STANDARD_PERSISTED.name().equals(eventType)) {
            return fromByteArray(data, ClpProductStandardPersistedEvent.class);
        }
        if (ProductEventType.PRODUCT_REMOVED.name().equals(eventType)) {
            return fromByteArray(data, ClpProductRemovedEvent.class);
        }

        if (ProfitabilityEventType.PROFITABILITY_CALCULATED.name().equals(eventType)) {
            return fromByteArray(data, ClpProfitabilityCalculatedEvent.class);
        }
        if (ProfitabilityEventType.PROFITABILITY_INPUT_PERSISTED.name().equals(eventType)) {
            return fromByteArray(data, ClpProfitabilityInputPersistedChangedEvent.class);
        }
        if (ProfitabilityEventType.PROFITABILITY_PERSISTED.name().equals(eventType)) {
            return fromByteArray(data, ClpProfitabilityPersistedEvent.class);
        }

        if (DecisionEventType.DECISION_CHANGED.name().equals(eventType)) {
            return fromByteArray(data, ClpDecisionChangedEvent.class);
        }
        if (DecisionEventType.FINAL_DECISION_MADE.name().equals(eventType)) {
            return fromByteArray(data, ClpFinalDecisionMadeEvent.class);
        }
        if (DecisionEventType.DECISION_PERSISTED.name().equals(eventType)) {
            return fromByteArray(data, ClpDecisionPersistedEvent.class);
        }

        if (CustomerEventType.CUSTOMER_UPDATED.name().equals(eventType)) {
            return fromByteArray(data, ClpCustomerGroupUpdatedEvent.class);
        }
        if (CustomerEventType.CUSTOMER_PERSISTED.name().equals(eventType)) {
            return fromByteArray(data, ClpCustomerGroupPersistedEvent.class);
        }

        throw new RuntimeException("Unknown event type: " + eventType);
    }

    @SneakyThrows
    private static String eventTypeFromByteArray(byte[] data) {
        ObjectNode jsonObject = MAPPER.readValue(data, ObjectNode.class);
        return jsonObject.get("type").asText();
    }

    @SneakyThrows
    private static <T extends ClpEvent> T fromByteArray(byte[] data, Class<T> toClass) {
        return MAPPER.readValue(data, toClass);
    }
}
