package com.example.eventcommons.event.product.snapshot;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Set;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type",
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = OverdraftSnapshot.class, name = "OVERDRAFT"),
        @JsonSubTypes.Type(value = GuaranteeLimitSnapshot.class, name = "GUARANTEE_LIMIT"),
        @JsonSubTypes.Type(value = GuaranteeProductSnapshot.class, name = "GUARANTEE_PRODUCT"),
})
public interface ProductSnapshot {
    Long getId();
    String getCustomerId();
    String getType();
    Long getOrderId();
    default Set<Long> getRefinancedProductIds() {
        return Set.of();
    }
    default Set<Long> getAbsorbedProductIds() {
        return Set.of();
    }
}
