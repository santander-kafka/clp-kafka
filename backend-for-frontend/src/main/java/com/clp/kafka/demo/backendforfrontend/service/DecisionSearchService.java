package com.clp.kafka.demo.backendforfrontend.service;

import com.clp.kafka.demo.backendforfrontend.domain.decision.DecisionDto;
import com.clp.kafka.demo.backendforfrontend.domain.decision.DecisionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class DecisionSearchService {
    private final DecisionRepository repository;

    public DecisionDto findByOrderId(Long orderId) {
        return repository.findByOrderId(orderId);
    }

    public List<DecisionDto> findAllByOrderIdIn(List<Long> orderIds) {
        return repository.findAllByOrderIdIn(orderIds);
    }
}
