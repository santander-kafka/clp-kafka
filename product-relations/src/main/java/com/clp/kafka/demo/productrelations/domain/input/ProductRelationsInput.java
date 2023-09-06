package com.clp.kafka.demo.productrelations.domain.input;

import java.util.Set;

public interface ProductRelationsInput {
    Long getTargetProductId();

    Long getSourceProductId();

    Set<RelationInput> getTargetRelations();
}