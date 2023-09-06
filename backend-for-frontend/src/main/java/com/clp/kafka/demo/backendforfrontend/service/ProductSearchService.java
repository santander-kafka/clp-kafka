package com.clp.kafka.demo.backendforfrontend.service;

import com.clp.kafka.demo.backendforfrontend.domain.product.ProductDto;
import com.clp.kafka.demo.backendforfrontend.domain.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class ProductSearchService {
    private final ProductRepository repository;

    public List<ProductDto> findAllByOrderId(Long orderId) {
        return repository.findAllByOrderId(orderId);
    }

    public List<ProductDto> findAllByOrderIdIn(List<Long> orderIds) {
        return repository.findAllByOrderIdIn(orderIds);
    }

    public ProductDto getById(Long productId) {
        return repository.findById(productId)
                .orElseThrow();
    }

}
