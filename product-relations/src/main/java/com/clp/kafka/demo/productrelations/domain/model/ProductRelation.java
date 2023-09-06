package com.clp.kafka.demo.productrelations.domain.model;

import com.clp.kafka.demo.productrelations.domain.input.ProductRelationsInput;
import com.clp.kafka.demo.productrelations.domain.input.RelationInput;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

import static java.util.Objects.nonNull;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ProductRelation {
    @Id
    @GeneratedValue
    private Long id;
    @Column
    @Enumerated(EnumType.STRING)
    private RelationType type;
    private Long sourceId;
    private Long targetId;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private Set<Long> toBeReplacedBy = new HashSet<>();

    @Builder.Default
    private boolean archival = false;

    public static ProductRelation from(ProductRelation relation, Long targetId) {
        ProductRelation result = new ProductRelation();
        result.type = relation.getType();
        result.sourceId = relation.getSourceId();
        result.toBeReplacedBy = new HashSet<>(relation.getToBeReplacedBy());
        result.targetId = targetId;
        return result;
    }

    public static ProductRelation from(RelationInput input) {
        ProductRelation result = new ProductRelation();
        result.type = RelationType.valueOf(input.getType().name());
        result.sourceId = input.getSourceId();
        result.targetId = input.getTargetId();
        result.toBeReplacedBy = new HashSet<>();
        return result;
    }

    public static ProductRelation modificationFrom(ProductRelationsInput input) {
        ProductRelation result = new ProductRelation();
        result.type = RelationType.MODIFICATION;
        result.sourceId = input.getSourceProductId();
        result.targetId = input.getTargetProductId();
        result.toBeReplacedBy = new HashSet<>();
        return result;
    }

    public static ProductRelation renewalFrom(ProductRelationsInput input) {
        ProductRelation result = new ProductRelation();
        result.type = RelationType.RENEWAL;
        result.sourceId = input.getSourceProductId();
        result.targetId = input.getTargetProductId();
        result.toBeReplacedBy = new HashSet<>();
        return result;
    }

    public static ProductRelation resignationFrom(ProductRelationsInput input) {
        ProductRelation result = new ProductRelation();
        result.type = RelationType.RESIGNATION;
        result.sourceId = input.getSourceProductId();
        result.targetId = input.getTargetProductId();
        result.toBeReplacedBy = new HashSet<>();
        return result;
    }

    public void markToBeReplacedBy(ProductRelationsInput input) {
        if (input.getSourceProductId().equals(targetId)) {
            toBeReplacedBy.add(input.getTargetProductId());
        }
    }

    public void clearMarkToBeReplacedBy(ProductRelationsInput input) {
        toBeReplacedBy.removeIf(id -> nonNull(id) && input.getTargetProductId().equals(id));
    }

    public boolean isSameTarget(Long productId) {
        return targetId.equals(productId);
    }

    public void archive() {
        archival = true;
    }

}
