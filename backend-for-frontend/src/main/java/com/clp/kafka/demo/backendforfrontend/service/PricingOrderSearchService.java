package com.clp.kafka.demo.backendforfrontend.service;

import com.clp.kafka.demo.backendforfrontend.domain.order.PricingOrderBaseDto;
import com.clp.kafka.demo.backendforfrontend.domain.order.PricingOrderDtoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class PricingOrderSearchService {
    private final PricingOrderDtoRepository pricingOrderDtoRepository;

    public List<PricingOrderBaseDto> findAllByGroupId(@PathVariable String groupId) {
        return pricingOrderDtoRepository.findAllByGroupId(groupId);
    }

    public PricingOrderBaseDto getById(Long orderId) {
        return pricingOrderDtoRepository.getById(orderId);
    }
}
