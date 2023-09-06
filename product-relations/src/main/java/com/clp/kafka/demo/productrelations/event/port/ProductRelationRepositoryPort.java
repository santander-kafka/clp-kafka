package com.clp.kafka.demo.productrelations.event.port;

import com.clp.kafka.demo.productrelations.domain.input.DefaultProductRelationsInput;
import com.example.eventcommons.event.relations.ProductRelationSnapshot;

import java.util.Set;

public interface ProductRelationRepositoryPort {
    Set<ProductRelationSnapshot> onProductCreated(DefaultProductRelationsInput from);

    Set<ProductRelationSnapshot> onProductUpdated(DefaultProductRelationsInput input);

    Set<ProductRelationSnapshot> onModificationCreated(DefaultProductRelationsInput input);

    Set<ProductRelationSnapshot> onRenewalCreated(DefaultProductRelationsInput input);

    Set<ProductRelationSnapshot> onResignationCreated(DefaultProductRelationsInput input);

    Set<ProductRelationSnapshot> onProductLaunchOrderCreated(DefaultProductRelationsInput input);

    Set<ProductRelationSnapshot> onProductLaunchedCreated(DefaultProductRelationsInput input);

    Set<ProductRelationSnapshot> onProductRejected(DefaultProductRelationsInput input);

    Set<ProductRelationSnapshot> onProductCanceled(DefaultProductRelationsInput input);

    Set<ProductRelationSnapshot> onProductRemoved(DefaultProductRelationsInput input);
}
