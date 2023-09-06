package com.clp.kafka.demo.backendforfrontend.domain.standard;

import com.example.eventcommons.event.productstandard.ProductStandardSnapshot;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface ProductStandardDtoRepository extends MongoRepository<ProductStandardDto, Long> {

    default List<ProductStandardDto> saveAll(Set<ProductStandardSnapshot> productStandardSnapshots) {
        return saveAll(productStandardSnapshots.stream().map(ProductStandardDto::from)
                .collect(Collectors.toSet()));
    }

    default List<ProductStandardDto> findAllByProductIdIn(List<Long> longs) {
        return findAllById(longs);
    }
}
