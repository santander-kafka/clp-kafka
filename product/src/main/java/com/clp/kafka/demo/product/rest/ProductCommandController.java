package com.clp.kafka.demo.product.rest;

import com.clp.kafka.demo.product.event.saga.model.DefaultProductSaga;
import com.clp.kafka.demo.product.service.ProductCommandHandler;
import com.clp.kafka.demo.product.service.ProductSearchService;
import com.example.eventcommons.event.product.form.ProductForm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductCommandController {
    private final ProductCommandHandler commandHandler;
    private final ProductSearchService searchService;

    @GetMapping
    public String ok() {
        return "OK";
    }

    @PostMapping
    public UUID createProduct(@RequestBody ProductForm productForm) {
        return commandHandler.handleCreateProductCommand(productForm);
    }

    @PutMapping
    public UUID updateProduct(@RequestBody ProductForm productForm) {
        return commandHandler.handleUpdateProductCommand(productForm);
    }

    @DeleteMapping("/{productId}")
    public UUID removeProduct(@PathVariable Long productId) {
        return commandHandler.handleRemoveProductCommand(productId);
    }

    @GetMapping("/process/state/{uuid}")
    public DefaultProductSaga getProcessState(@PathVariable String uuid) {
        return searchService.getProcessState(UUID.fromString(uuid));
    }
}
