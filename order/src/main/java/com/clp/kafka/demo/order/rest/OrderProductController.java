package com.clp.kafka.demo.order.rest;

import com.clp.kafka.demo.order.event.saga.create.OrderCreationSaga;
import com.clp.kafka.demo.order.event.OrderSagaState;
import com.clp.kafka.demo.order.service.OrderCommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController()
@RequestMapping("/order/product")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderProductController {
    private final OrderCommandHandler commandHandler;
    private final OrderSearchService searchService;

    @PutMapping("/")
    public UUID createOrder(@RequestBody String groupId) {
        return commandHandler.handleCreateOrderRequest(groupId);
    }

    @GetMapping("/process/state/{uuid}")
    public OrderSagaState getProcessState(@PathVariable String uuid) {
        return searchService.getProcessState(UUID.fromString(uuid));
    }
}
