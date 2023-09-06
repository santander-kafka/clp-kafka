package com.clp.kafka.demo.productrelations.domain.model;

import com.clp.kafka.demo.productrelations.domain.input.DefaultProductRelationsInput;
import com.clp.kafka.demo.productrelations.domain.input.ProductRelationsInput;
import com.example.eventcommons.event.relations.ProductRelationSnapshot;
import lombok.Builder;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
@Getter
public class ProductRelations {
    @Builder.Default
    private Set<ProductRelation> relations = new HashSet<>();
    @Builder.Default
    private Set<ProductRelation> removedRelations = new HashSet<>();

    public void onProductCreated(ProductRelationsInput input) {
        replaceTargetRelations(input);
    }

    public void onProductUpdated(ProductRelationsInput input) {
        replaceTargetRelations(input);
    }

    public void onModificationCreated(ProductRelationsInput input) {
        markRelationsToBeReplacedBy(input);
        addModificationRelation(input);
    }

    public void onRenewalCreated(ProductRelationsInput input) {
        markRelationsToBeReplacedBy(input);
        addRenewalRelation(input);
    }

    public void onResignationCreated(ProductRelationsInput input) {
        markRelationsToBeReplacedBy(input);
        addResignationRelation(input);
    }

    public void onProductLaunchOrderCreated(ProductRelationsInput input) {
        copyRelations(input);
        archiveOtherThanTargetRelations(input);
    }

    public void onProductLaunchedCreated(ProductRelationsInput input) {
        copyRelations(input);
        clearOldRelations(input);
    }

    public void onProductRemoved(ProductRelationsInput input) {
        archiveTargetRelations(input);
        clearToBeReplacedBy(input);
    }

    public void onProductRejected(ProductRelationsInput input) {
        archiveTargetRelations(input);
        clearToBeReplacedBy(input);
    }

    public void onProductCanceled(ProductRelationsInput input) {
        archiveTargetRelations(input);
        clearToBeReplacedBy(input);
    }

    public Set<ProductRelationSnapshot> snapshot() {
        return relations.stream().map(relation -> ProductRelationSnapshot.builder()
                        .id(relation.getId())
                        .sourceId(relation.getSourceId())
                        .targetId(relation.getTargetId())
                        .active(relation.getToBeReplacedBy().isEmpty())
                        .archival(relation.isArchival())
                        .build())
                .collect(Collectors.toSet());
    }

    private void addModificationRelation(ProductRelationsInput input) {
        relations.add(ProductRelation.modificationFrom(input));
    }

    public void onCorrectionCreated(DefaultProductRelationsInput input) {
        copyRelations(input);
        markRelationsToBeReplacedBy(input);
    }

    private void addRenewalRelation(ProductRelationsInput input) {
        relations.add(ProductRelation.renewalFrom(input));
    }

    private void addResignationRelation(ProductRelationsInput input) {
        relations.add(ProductRelation.resignationFrom(input));
    }

    private void replaceTargetRelations(ProductRelationsInput input) {
        clearTargetRelations(input);
        relations.addAll(input.getTargetRelations().stream().map(ProductRelation::from)
                .collect(Collectors.toSet()));
    }

    private void markRelationsToBeReplacedBy(ProductRelationsInput input) {
        relations.stream().filter(relation -> !relation.isArchival() && !relation.isSameTarget(input.getTargetProductId()))
                .forEach(relation -> relation.markToBeReplacedBy(input));
    }

    private void archiveTargetRelations(ProductRelationsInput input) {
        relations.stream().filter(relation -> relation.isSameTarget(input.getTargetProductId()))
                .forEach(ProductRelation::archive);
    }

    private void archiveOtherThanTargetRelations(ProductRelationsInput input) {
        relations.stream().filter(relation -> !relation.isSameTarget(input.getTargetProductId()))
                .forEach(ProductRelation::archive);
    }

    private void clearTargetRelations(ProductRelationsInput input) {
        removedRelations.addAll(relations.stream().filter(relation -> relation.isSameTarget(input.getTargetProductId())).toList());
    }

    private void clearOldRelations(ProductRelationsInput input) {
        removedRelations.addAll(relations.stream().filter(relation -> relation.isSameTarget(input.getSourceProductId())).toList());
    }

    private void clearToBeReplacedBy(ProductRelationsInput input) {
        relations.forEach(relation -> relation.clearMarkToBeReplacedBy(input));
    }

    private void copyRelations(ProductRelationsInput input) {
        Set<ProductRelation> newRelations = relations.stream()
                .filter(relation -> relation.isSameTarget(input.getSourceProductId()))
                .map(relation -> ProductRelation.from(relation, input.getTargetProductId()))
                .collect(Collectors.toSet());
        relations.addAll(newRelations);
    }

    public Set<ProductRelation> getActiveRelations() {
        return getRelations().stream()
                .filter(relation -> !relation.isArchival() && relation.getToBeReplacedBy().size() == 0)
                .collect(Collectors.toSet());
    }

}
