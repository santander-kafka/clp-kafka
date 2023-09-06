package com.clp.kafka.demo.customer.event;

import com.clp.kafka.demo.customer.rest.UpdateCustomerGroupCommand;
import com.clp.kafka.demo.customer.service.CustomerService;
import com.example.eventcommons.event.customer.CustomerGroupSnapshot;
import com.example.eventcommons.event.customer.event.ClpCustomerGroupUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;
import java.util.function.Function;

@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class CustomerEventHandlers {
    public static final String CUSTOMERS_STREAM_STORE = "latest-customer-store";
    private final CustomerService customerService;

    @Bean
    public Function<KStream<String, UpdateCustomerGroupCommand>, KStream<String, ClpCustomerGroupUpdatedEvent>> customers() {
        return commands -> commands
                .map((sagaId, command) -> {
                    log.info("CustomerEventHandlers: {} {}", sagaId, command);
                    CustomerGroupSnapshot snapshot = customerService.upsertGroup(command.getGroupId());
                    return new KeyValue<>(sagaId, ClpCustomerGroupUpdatedEvent.builder()
                            .sagaId(UUID.fromString(sagaId))
                            .snapshot(snapshot)
                            .build());
                });
    }
}
