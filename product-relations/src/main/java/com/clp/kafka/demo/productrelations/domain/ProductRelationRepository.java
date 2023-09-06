package com.clp.kafka.demo.productrelations.domain;

import com.clp.kafka.demo.productrelations.domain.model.ProductRelation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface ProductRelationRepository extends JpaRepository<ProductRelation, Long> {

    Collection<ProductRelation> findAllBySourceIdInOrTargetIdIn(Collection<Long> targetIds, Collection<Long> sourceIds);
}
