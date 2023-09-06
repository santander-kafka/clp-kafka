package com.clp.kafka.demo.productrelations.domain.input;

import com.example.eventcommons.event.product.snapshot.ProductSnapshot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefaultProductRelationsInput implements ProductRelationsInput {
    private Long targetProductId;
    private Long sourceProductId;
    @Builder.Default
    private Set<RelationInput> targetRelations = new HashSet<>();

    public static DefaultProductRelationsInput from(ProductSnapshot snapshot) {
        DefaultProductRelationsInput result = new DefaultProductRelationsInput();
        result.targetProductId = snapshot.getId();
        result.targetRelations = getTargetRelations(snapshot);
        return result;
    }

    public static DefaultProductRelationsInput from(ProductSnapshot targetSnapshot, ProductSnapshot sourceSnapshot) {
        DefaultProductRelationsInput result = new DefaultProductRelationsInput();
        result.targetProductId = targetSnapshot.getId();
        result.targetRelations = getTargetRelations(targetSnapshot);
        result.sourceProductId = sourceSnapshot.getId();
        return result;
    }

    private static Set<RelationInput> getTargetRelations(ProductSnapshot snapshot) {
        return Stream.concat(
                snapshot.getRefinancedProductIds().stream().map(id -> RelationInput.builder().targetId(snapshot.getId()).sourceId(id)
                        .type(InputRelationType.REFINANCING).build()),
                snapshot.getAbsorbedProductIds().stream().map(id -> RelationInput.builder().targetId(snapshot.getId()).sourceId(id)
                        .type(InputRelationType.ABSORPTION).build())
        ).collect(Collectors.toSet());
    }
}