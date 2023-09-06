package com.clp.kafka.demo.order.rest;

import com.clp.kafka.demo.order.event.saga.create.OrderCreationSaga;
import com.clp.kafka.demo.order.event.OrderSagaState;
import com.clp.kafka.demo.order.service.OrderCommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderController {
    private final OrderCommandHandler commandHandler;
    private final OrderSearchService searchService;

    @PostMapping("/{groupId}")
    public UUID createOrder(@PathVariable String groupId) {
        return commandHandler.handleCreateOrderRequest(groupId);
    }

    @GetMapping("/process/state/{uuid}")
    public OrderSagaState getProcessState(@PathVariable String uuid) {
        return searchService.getProcessState(UUID.fromString(uuid));
    }
}
