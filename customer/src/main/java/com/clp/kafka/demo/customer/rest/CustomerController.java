package com.clp.kafka.demo.customer.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomerController {
    private final StreamBridge streamBridge;

    @PutMapping("/{groupId}")
    public UUID updateGroup(@PathVariable String groupId) {
        UUID sagaId = UUID.randomUUID();
        UpdateCustomerGroupCommand command = UpdateCustomerGroupCommand.builder()
                .id(UUID.randomUUID())
                .sagaId(sagaId)
                .groupId(groupId).build();
        streamBridge.send("commands-out-0", MessageBuilder.withPayload(command)
                .setHeader(KafkaHeaders.KEY, sagaId.toString().getBytes())
                .build());
        return sagaId;
    }
}
