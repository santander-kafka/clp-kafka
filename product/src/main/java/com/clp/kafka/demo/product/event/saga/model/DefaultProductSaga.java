package com.clp.kafka.demo.product.event.saga.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DefaultProductSaga implements ProductSaga {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private UUID processId;
    private boolean error = false;
    private boolean successProductPersisted = false;
    private boolean successProductRelationsPersisted = false;
    private boolean successProductStandardPersisted = false;
    private boolean profitabilityInputPersisted = false;
    private boolean decisionUpdated = false;

    public static DefaultProductSaga start() {
        DefaultProductSaga saga = new DefaultProductSaga();
        saga.processId = UUID.randomUUID();
        return saga;
    }

    public DefaultProductSaga successProductPersisted() {
        if (isFinished()) {
            return this;
        }
        successProductPersisted = true;
        return this;
    }


    public DefaultProductSaga successProductRelationsPersisted() {
        if (isFinished()) {
            return this;
        }
        successProductRelationsPersisted = true;
        return this;
    }

    public DefaultProductSaga successProductStandardPersisted() {
        if (isFinished()) {
            return this;
        }
        successProductStandardPersisted = true;
        return this;
    }


    public DefaultProductSaga profitabilityInputPersisted() {
        if (isFinished()) {
            return this;
        }
        profitabilityInputPersisted = true;
        return this;
    }

    public DefaultProductSaga decisionUpdated() {
        if (isFinished()) {
            return this;
        }
        decisionUpdated = true;
        return this;
    }

    public UUID getProcessId() {
        return processId;
    }

    public ProductSagaState getState() {
        if (error) {
            return ProductSagaState.ERROR;
        }
        if (successProductPersisted
                && successProductRelationsPersisted
                && successProductStandardPersisted
                && profitabilityInputPersisted
                && decisionUpdated) {
            return ProductSagaState.SUCCESS;
        }
        return ProductSagaState.PENDING;
    }

    private boolean isFinished() {
        return !ProductSagaState.PENDING.equals(getState());
    }
}
