package com.clp.kafka.demo.product.domain.behaviour;

import com.clp.kafka.demo.product.domain.WithSnapshot;
import com.example.eventcommons.event.product.form.ProductForm;

public interface UpdateBehaviour extends WithSnapshot {
    WithSnapshot updateFromForm(ProductForm productForm);
}
