package com.clp.kafka.demo.product.domain;

import com.clp.kafka.demo.product.domain.behaviour.*;
import com.clp.kafka.demo.product.domain.guaranteelimit.GuaranteeLimit;
import com.clp.kafka.demo.product.domain.guaranteeproduct.GuaranteeProduct;
import com.clp.kafka.demo.product.domain.overdraft.Overdraft;
import com.clp.kafka.demo.product.service.port.ProductRepositoryPort;
import com.example.eventcommons.event.product.form.GuaranteeLimitForm;
import com.example.eventcommons.event.product.form.GuaranteeProductForm;
import com.example.eventcommons.event.product.form.OverdraftForm;
import com.example.eventcommons.event.product.form.ProductForm;
import com.example.eventcommons.event.product.snapshot.ProductSnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.clp.kafka.demo.product.domain.ProductType.valueOf;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ProductDomainRepository implements ProductRepositoryPort {
    private final ProductRepository repository;

    public ProductSnapshot createNew(ProductForm productForm) {
        return saveAndFlush(createInstance(productForm));
    }

    @Override
    public ProductSnapshot updateFromForm(ProductForm productForm) {
        return findById(productForm.getId(), UpdateBehaviour.class)
                .map(product -> product.updateFromForm(productForm))
                .map(this::saveAndFlush)
                .orElseThrow();
    }

    @Override
    public ProductSnapshot renew(Long productId) {
        return findById(productId, RenewBehaviour.class)
                .map(RenewBehaviour::renew)
                .map(this::saveAndFlush)
                .orElseThrow();
    }

    @Override
    public ProductSnapshot modify(Long productId) {
        return findById(productId, ModifyBehaviour.class)
                .map(ModifyBehaviour::modify)
                .map(this::saveAndFlush)
                .orElseThrow();
    }

    @Override
    public ProductSnapshot resign(Long productId) {
        return findById(productId, ResignBehaviour.class)
                .map(ResignBehaviour::resign)
                .map(this::saveAndFlush)
                .orElseThrow();
    }

    @Override
    public ProductSnapshot cancel(Long productId) {
        return findById(productId, CancelBehaviour.class)
                .map(CancelBehaviour::cancel)
                .map(this::saveAndFlush)
                .orElseThrow();
    }

    @Override
    public ProductSnapshot removeProduct(Long productId) {
        WithSnapshot product = findById(productId, WithSnapshot.class).orElseThrow();
        repository.deleteById(productId);
        return product.snapshot();
    }

    public WithSnapshot createInstance(ProductForm productForm) {
        String type = productForm.getType();
        switch (valueOf(type)) {
            case OVERDRAFT:
                return Overdraft.createFromForm((OverdraftForm) productForm);
            case GUARANTEE_LIMIT: {
                return GuaranteeLimit.createFromForm((GuaranteeLimitForm) productForm);
            }
            case GUARANTEE_PRODUCT: {
                return GuaranteeProduct.createFromForm((GuaranteeProductForm) productForm);
            }
            default:
                throw new UnsupportedOperationException(type);
        }
    }

    public <T> Optional<T> findById(Long productId, Class<T> clazz) {
        return repository.findById(productId)
                .filter(clazz::isInstance)
                .map(clazz::cast);
    }

    public ProductSnapshot saveAndFlush(WithSnapshot withSnapshot) {
        return repository.saveAndFlush((BaseProductData) withSnapshot).snapshot();
    }
}
