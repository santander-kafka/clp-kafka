package com.clp.kafka.demo.decision.service;

import com.clp.kafka.demo.decision.domain.DecisionApproverRepository;
import com.clp.kafka.demo.decision.event.DecisionEventsHandler;
import com.example.eventcommons.event.decision.event.DecisionSnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DecisionService {
    private final DecisionApproverRepository repository;
    private final DecisionEventsHandler eventsHandler;

    @Transactional
    public UUID approveDecision(Long orderId) {
        DecisionSnapshot decisionSnapshot = repository.approveDecision(orderId);
        UUID sagaId = UUID.randomUUID();
        return eventsHandler.sendFinalDecisionMessageEvent(decisionSnapshot, sagaId);
    }

    @Transactional
    public UUID rejectDecision(Long orderId) {
        DecisionSnapshot decisionSnapshot = repository.rejectDecision(orderId);
        UUID sagaId = UUID.randomUUID();
        return eventsHandler.sendFinalDecisionMessageEvent(decisionSnapshot, sagaId);
    }
}
