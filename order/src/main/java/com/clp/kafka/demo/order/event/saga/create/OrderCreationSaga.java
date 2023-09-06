package com.clp.kafka.demo.order.event.saga.create;


import com.clp.kafka.demo.order.event.OrderSagaState;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderCreationSaga {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private UUID processId;
    private boolean orderCreated = false;
    private boolean orderPersisted = false;
    private boolean error = false;
    private String errorMessage = null;

    private OrderCreationSaga(UUID processId) {
        this.processId = processId;
    }

    public static OrderCreationSaga start(UUID processId) {
        return new OrderCreationSaga(processId);
    }

    public void topicCreationError(Throwable exception) {
        error = true;
        errorMessage = exception.getMessage();
    }

    public void successOrderCreated() {
        if (isSuccess()) {
            return;
        }
        orderCreated = true;
    }


    public void successOrderPersisted() {
        if (isSuccess()) {
            return;
        }
        orderPersisted = true;
    }


    public UUID getProcessId() {
        return processId;
    }


    private boolean isSuccess() {
        return OrderSagaState.SUCCESS.equals(getState());
    }

    public OrderSagaState getState() {
        if (error) {
            return OrderSagaState.ERROR;
        }
        if (orderCreated && orderPersisted) {
            return OrderSagaState.SUCCESS;
        }
        return OrderSagaState.PENDING;
    }

    private String getErrorMessage() {
        return errorMessage;
    }

}
