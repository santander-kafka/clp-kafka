package com.clp.kafka.demo.product.service;

import com.clp.kafka.demo.product.event.ProductEventHandler;
import com.clp.kafka.demo.product.service.port.ProductRepositoryPort;
import com.example.eventcommons.event.product.form.ProductForm;
import com.example.eventcommons.event.product.snapshot.ProductSnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductCommandHandler {
    private final ProductRepositoryPort productRepository;
    private final ProductEventHandler eventHandler;

    @Transactional
    public UUID handleCreateProductCommand(ProductForm productForm) {
        ProductSnapshot snapshot = productRepository.createNew(productForm);
        return eventHandler.handleProductCreated(snapshot);
    }

    public UUID handleUpdateProductCommand(ProductForm productForm) {
        ProductSnapshot snapshot = productRepository.updateFromForm(productForm);
        return eventHandler.handleProductUpdated(snapshot);
    }

    public UUID handleRemoveProductCommand(Long productId) {
        ProductSnapshot snapshot = productRepository.removeProduct(productId);
        return eventHandler.handleProductRemoved(snapshot);
    }
}
