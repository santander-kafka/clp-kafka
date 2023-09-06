package com.clp.kafka.demo.backendforfrontend.service;

import com.clp.kafka.demo.backendforfrontend.domain.product.ProductDto;
import com.clp.kafka.demo.backendforfrontend.domain.product.ProductRepository;
import com.clp.kafka.demo.backendforfrontend.domain.profitability.ProfitabilityDto;
import com.clp.kafka.demo.backendforfrontend.domain.relation.ProductRelationDto;
import com.clp.kafka.demo.backendforfrontend.domain.relation.ProductRelationDtoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductRelationSearchService {
    private final ProductRelationDtoRepository repository;

    public List<ProductRelationDto> getAllBySourceOrTargetIdIn(List<Long> ids) {
        return repository.findAllBySourceIdInOrTargetIdIn(ids, ids);
    }
}
