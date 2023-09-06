package com.example.eventcommons.event.product.form;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type",
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = OverdraftForm.class, name = "OVERDRAFT"),
        @JsonSubTypes.Type(value = GuaranteeLimitForm.class, name = "GUARANTEE_LIMIT"),
        @JsonSubTypes.Type(value = GuaranteeProductForm.class, name = "GUARANTEE_PRODUCT"),
})
public interface ProductForm {
    Long getId();
    String getCustomerId();
    Long getOrderId();
    String getType();
}
