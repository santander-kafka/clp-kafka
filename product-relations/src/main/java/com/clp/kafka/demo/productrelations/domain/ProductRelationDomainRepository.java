package com.clp.kafka.demo.productrelations.domain;

import com.clp.kafka.demo.productrelations.domain.input.DefaultProductRelationsInput;
import com.clp.kafka.demo.productrelations.domain.model.ProductRelations;
import com.clp.kafka.demo.productrelations.event.port.ProductRelationRepositoryPort;
import com.example.eventcommons.event.relations.ProductRelationSnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Optional.ofNullable;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductRelationDomainRepository implements ProductRelationRepositoryPort {
    private final ProductRelationRepository productRelationRepository;

    @Override
    @Transactional
    public Set<ProductRelationSnapshot> onProductCreated(DefaultProductRelationsInput input) {
        ProductRelations productRelations = getProductRelations(input);
        productRelations.onProductCreated(input);
        save(productRelations);
        return productRelations.snapshot();
    }

    @Override
    @Transactional
    public Set<ProductRelationSnapshot> onProductUpdated(DefaultProductRelationsInput input) {
        ProductRelations productRelations = getProductRelations(input);
        productRelations.onProductUpdated(input);
        save(productRelations);
        return productRelations.snapshot();
    }

    @Override
    @Transactional
    public Set<ProductRelationSnapshot> onModificationCreated(DefaultProductRelationsInput input) {
        ProductRelations productRelations = getProductRelations(input);
        productRelations.onModificationCreated(input);
        save(productRelations);
        return productRelations.snapshot();
    }

    @Override
    @Transactional
    public Set<ProductRelationSnapshot> onRenewalCreated(DefaultProductRelationsInput input) {
        ProductRelations productRelations = getProductRelations(input);
        productRelations.onRenewalCreated(input);
        save(productRelations);
        return productRelations.snapshot();
    }

    @Override
    @Transactional
    public Set<ProductRelationSnapshot> onResignationCreated(DefaultProductRelationsInput input) {
        ProductRelations productRelations = getProductRelations(input);
        productRelations.onResignationCreated(input);
        save(productRelations);
        return productRelations.snapshot();
    }

    @Override
    @Transactional
    public Set<ProductRelationSnapshot> onProductLaunchOrderCreated(DefaultProductRelationsInput input) {
        ProductRelations productRelations = getProductRelations(input);
        productRelations.onProductLaunchOrderCreated(input);
        save(productRelations);
        return productRelations.snapshot();
    }

    @Override
    @Transactional
    public Set<ProductRelationSnapshot> onProductLaunchedCreated(DefaultProductRelationsInput input) {
        ProductRelations productRelations = getProductRelations(input);
        productRelations.onProductLaunchedCreated(input);
        save(productRelations);
        return productRelations.snapshot();
    }

    @Override
    @Transactional
    public Set<ProductRelationSnapshot> onProductRejected(DefaultProductRelationsInput input) {
        ProductRelations productRelations = getProductRelations(input);
        productRelations.onProductRejected(input);
        save(productRelations);
        return productRelations.snapshot();
    }

    @Override
    @Transactional
    public Set<ProductRelationSnapshot> onProductCanceled(DefaultProductRelationsInput input) {
        ProductRelations productRelations = getProductRelations(input);
        productRelations.onProductCanceled(input);
        save(productRelations);
        return productRelations.snapshot();
    }

    @Override
    @Transactional
    public Set<ProductRelationSnapshot> onProductRemoved(DefaultProductRelationsInput input) {
        ProductRelations productRelations = getProductRelations(input);
        productRelations.onProductRemoved(input);
        save(productRelations);
        return productRelations.snapshot();
    }

    private ProductRelations getProductRelations(DefaultProductRelationsInput input) {
        return ofNullable(input.getSourceProductId()).map((id) ->
            ProductRelations.builder()
                    .relations(new HashSet<>(productRelationRepository.findAllBySourceIdInOrTargetIdIn(List.of(input.getSourceProductId(), input.getTargetProductId()), List.of(input.getTargetProductId(), input.getSourceProductId()))))
                    .build()
        ).orElse(ProductRelations.builder()
                .relations(new HashSet<>(productRelationRepository.findAllBySourceIdInOrTargetIdIn(List.of(input.getTargetProductId()), List.of(input.getTargetProductId()))))
                .build());
    }


    private void save(ProductRelations productRelations) {
        productRelationRepository.saveAllAndFlush(productRelations.getRelations());
        productRelationRepository.deleteAll(productRelations.getRemovedRelations());
    }
}
