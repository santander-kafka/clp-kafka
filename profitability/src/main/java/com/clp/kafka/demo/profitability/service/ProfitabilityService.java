package com.clp.kafka.demo.profitability.service;

import com.clp.kafka.demo.profitability.domain.ProfitabilityInput;
import com.clp.kafka.demo.profitability.domain.ProfitabilityInputRepository;
import com.clp.kafka.demo.profitability.domain.ProfitabilityProductInput;
import com.clp.kafka.demo.profitability.event.ProfitabilityEvents;
import com.example.eventcommons.event.global.ClpEvent;
import com.example.eventcommons.event.profitability.event.ClpProfitabilityCalculatedEvent;
import com.example.eventcommons.event.profitability.event.ProfitabilitySnapshot;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProfitabilityService {
    private final KafkaTemplate<String, ClpEvent> defaultKafkaTemplate;
    private final ProfitabilityInputRepository profitabilityInputRepository;

    @Transactional
    public void updateInput(String orderId, ProfitabilityEvents events) {
        profitabilityInputRepository.deleteAllByOrderId(Long.valueOf(orderId));
        profitabilityInputRepository.saveAndFlush(ProfitabilityInput.from(Long.valueOf(orderId), events));
    }

    public List<String> calculateProfitability(Long orderId) {
        List<String> pricings = getPricings(orderId);
        sendProfitabilityCalculatedEvent(orderId, pricings);
        return pricings;
    }

    public List<String> getPricings(Long orderId) {
        ProfitabilityInput input = profitabilityInputRepository.getByOrderId(orderId);
        Collection<ProfitabilityProductInput> inputProducts = input.getProducts();
        // SEND CALC REQUEST
        return inputProducts.stream()
                .map(product -> product.getAmount().compareTo(BigDecimal.TEN) > 0 ? "RED" : "GREEN")
                .collect(Collectors.toList());
    }

    private void sendProfitabilityCalculatedEvent(Long orderId, List<String> pricings) {
        ClpEvent event = ClpProfitabilityCalculatedEvent.builder()
                .sagaId(UUID.randomUUID())
                .snapshot(ProfitabilitySnapshot.builder()
                        .pricings(pricings)
                        .orderId(orderId)
                        .build())
                .build();

        defaultKafkaTemplate.send(new ProducerRecord<>(event.getDefaultTopic(), event.getDefaultKey(), event));
    }
}
