package com.clp.kafka.demo.backendforfrontend.service;

import com.clp.kafka.demo.backendforfrontend.domain.profitability.ProfitabilityDto;
import com.clp.kafka.demo.backendforfrontend.domain.profitability.ProfitabilityDtoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class ProfitabilitSerachService {
    private final ProfitabilityDtoRepository productRepository;

    public List<ProfitabilityDto> findAllByOrderIdIn(List<Long> orderIds) {
        return productRepository.getByOrderIdIn(orderIds);
    }

    public ProfitabilityDto getByOrderId(Long orderId) {
        return productRepository.getByOrderId(orderId);
    }
}
