package com.example.eventcommons.event.product;

import java.util.UUID;

public class ProductKafkaConstants {
    public static final String PRODUCT_CHANGED_KEY_TEMPLATE = "PRODUCT_CHANGED_";

    public static String getProductChangedMessageKey(UUID sagaId) {
        return PRODUCT_CHANGED_KEY_TEMPLATE + sagaId;
    }

    public static final String PRODUCT_PERSISTED_KEY_TEMPLATE = "PRODUCT_PERSISTED_";

    public static String getProductPersistedMessageKey(Long productId) {
        return PRODUCT_PERSISTED_KEY_TEMPLATE + productId.toString();
    }

    public static final String RELATIONS_UPDATED_KEY_TEMPLATE = "RELATIONS_PERSISTED";

    public static String getRelationUpdatedMessageKey(Long productId) {
        return RELATIONS_UPDATED_KEY_TEMPLATE + productId.toString();
    }
}
