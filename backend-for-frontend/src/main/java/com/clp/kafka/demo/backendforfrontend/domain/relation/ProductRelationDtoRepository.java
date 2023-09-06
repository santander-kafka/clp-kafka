package com.clp.kafka.demo.backendforfrontend.domain.relation;

import com.example.eventcommons.event.relations.ProductRelationSnapshot;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface ProductRelationDtoRepository extends MongoRepository<ProductRelationDto, Long> {

    default List<ProductRelationDto> saveAll(Set<ProductRelationSnapshot> productRelationSnapshots) {
        Set<ProductRelationDto> dtos = productRelationSnapshots.stream()
                .map(ProductRelationDto::from)
                .collect(Collectors.toSet());
        return saveAll(dtos);
    }

    List<ProductRelationDto> findAllBySourceIdInOrTargetIdIn(Collection<Long> targetIds, Collection<Long> sourceIds);
}
