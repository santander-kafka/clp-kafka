package com.example.eventcommons.event.global;

import com.example.eventcommons.event.customer.event.ClpCustomerGroupPersistedEvent;
import com.example.eventcommons.event.customer.event.ClpCustomerGroupUpdatedEvent;
import com.example.eventcommons.event.decision.event.ClpDecisionChangedEvent;
import com.example.eventcommons.event.decision.event.ClpDecisionPersistedEvent;
import com.example.eventcommons.event.decision.event.ClpFinalDecisionMadeEvent;
import com.example.eventcommons.event.order.event.ClpOrderCreatedEvent;
import com.example.eventcommons.event.order.event.ClpOrderPersistedEvent;
import com.example.eventcommons.event.product.event.ClpProductCreatedEvent;
import com.example.eventcommons.event.product.event.ClpProductPersistedEvent;
import com.example.eventcommons.event.product.event.ClpProductRemovedEvent;
import com.example.eventcommons.event.productstandard.ClpProductStandardChangedEvent;
import com.example.eventcommons.event.productstandard.ClpProductStandardPersistedEvent;
import com.example.eventcommons.event.profitability.event.ClpProfitabilityCalculatedEvent;
import com.example.eventcommons.event.profitability.event.ClpProfitabilityPersistedEvent;
import com.example.eventcommons.event.relations.ClpProductRelationsChangedEvent;
import com.example.eventcommons.event.relations.ClpProductRelationsPersistedEvent;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.UUID;



@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ClpCustomerGroupUpdatedEvent.class, name = "CUSTOMER_UPDATED"),
        @JsonSubTypes.Type(value = ClpCustomerGroupPersistedEvent.class, name = "CUSTOMER_PERSISTED"),

        @JsonSubTypes.Type(value = ClpOrderCreatedEvent.class, name = "ORDER_CREATED"),
        @JsonSubTypes.Type(value = ClpOrderPersistedEvent.class, name = "ORDER_PERSISTED"),

        @JsonSubTypes.Type(value = ClpProductCreatedEvent.class, name = "PRODUCT_CREATED"),
        @JsonSubTypes.Type(value = ClpProductPersistedEvent.class, name = "PRODUCT_PERSISTED"),
        @JsonSubTypes.Type(value = ClpProductRemovedEvent.class, name = "PRODUCT_REMOVED"),

        @JsonSubTypes.Type(value = ClpDecisionChangedEvent.class, name = "DECISION_CHANGED"),
        @JsonSubTypes.Type(value = ClpDecisionPersistedEvent.class, name = "DECISION_PERSISTED"),
        @JsonSubTypes.Type(value = ClpFinalDecisionMadeEvent.class, name = "FINAL_DECISION_MADE"),

        @JsonSubTypes.Type(value = ClpProfitabilityCalculatedEvent.class, name = "PROFITABILITY_CALCULATED"),
        @JsonSubTypes.Type(value = ClpProfitabilityPersistedEvent.class, name = "PROFITABILITY_PERSISTED"),

        @JsonSubTypes.Type(value = ClpProductRelationsChangedEvent.class, name = "PRODUCT_RELATIONS_CHANGED"),
        @JsonSubTypes.Type(value = ClpProductRelationsPersistedEvent.class, name = "PRODUCT_RELATIONS_PERSISTED"),

        @JsonSubTypes.Type(value = ClpProductStandardChangedEvent.class, name = "PRODUCT_STANDARD_CHANGED"),
        @JsonSubTypes.Type(value = ClpProductStandardPersistedEvent.class, name = "PRODUCT_STANDARD_PERSISTED")

})
public interface ClpEvent {
    UUID getId();
    UUID getSagaId();

    @JsonIgnore
    Enum getType();

    @JsonIgnore
    default String getDefaultKey() {
        return getSagaId().toString();
    }

    @JsonIgnore
    String getDefaultTopic();

    @JsonIgnore
    default String getTopicTemplate() {
        return "CLP_ORDER_";
    }
}
