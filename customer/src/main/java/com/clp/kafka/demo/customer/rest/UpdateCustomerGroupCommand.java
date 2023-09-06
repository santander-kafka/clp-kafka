package com.clp.kafka.demo.customer.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class UpdateCustomerGroupCommand {
    @Builder.Default
    private UUID id = UUID.randomUUID();
    private String groupId;
    private UUID sagaId;
}
