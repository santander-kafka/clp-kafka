package com.clp.kafka.demo.product.service.port;

import com.example.eventcommons.event.product.form.ProductForm;
import com.example.eventcommons.event.product.snapshot.ProductSnapshot;


public interface ProductRepositoryPort {
    ProductSnapshot createNew(ProductForm productForm);

    ProductSnapshot updateFromForm(ProductForm productForm);

    ProductSnapshot renew(Long productId);

    ProductSnapshot modify(Long productId);

    ProductSnapshot resign(Long productId);

    ProductSnapshot cancel(Long productId);

    ProductSnapshot removeProduct(Long productId);
}
