package com.clp.kafka.demo.profitability.rest;

import com.clp.kafka.demo.profitability.service.ProfitabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/order/")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProfitabilityController {
    private final ProfitabilityService service;

    @GetMapping("{orderId}/profitability")
    public List<String> getProfitability(@PathVariable Long orderId) {
        return service.calculateProfitability(orderId);
    }

}
