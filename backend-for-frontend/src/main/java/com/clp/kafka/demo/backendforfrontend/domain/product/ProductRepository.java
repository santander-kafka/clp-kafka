package com.clp.kafka.demo.backendforfrontend.domain.product;

import com.example.eventcommons.event.product.snapshot.ProductSnapshot;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductRepository extends MongoRepository<ProductDto, Long> {
    List<ProductDto> findAllByOrderId(Long orderId);

    default ProductDto upsert(ProductSnapshot productSnapshot) {
        return save(ProductDto.from(productSnapshot));
    }

    List<ProductDto> findAllByOrderIdIn(List<Long> orderIds);
}
