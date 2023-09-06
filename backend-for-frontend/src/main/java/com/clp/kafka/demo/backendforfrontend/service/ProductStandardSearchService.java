package com.clp.kafka.demo.backendforfrontend.service;

import com.clp.kafka.demo.backendforfrontend.domain.standard.ProductStandardDto;
import com.clp.kafka.demo.backendforfrontend.domain.standard.ProductStandardDtoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductStandardSearchService {
    private final ProductStandardDtoRepository repository;

    List<ProductStandardDto> findAllByProductIdIn(List<Long> ids) {
        return repository.findAllByProductIdIn(ids);
    }
}
