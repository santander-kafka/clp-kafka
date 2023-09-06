package com.clp.kafka.demo.decision.rest;

import com.clp.kafka.demo.decision.service.DecisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/order/decision")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DecisionController {
    private final DecisionService service;

    @PutMapping("/approval/{orderId}")
    public UUID approveDecision(@PathVariable Long orderId) {
        return service.approveDecision(orderId);
    }

    @PutMapping("/rejection/{orderId}")
    public UUID rejectDecision(@PathVariable Long orderId) {
        return service.rejectDecision(orderId);
    }
}
