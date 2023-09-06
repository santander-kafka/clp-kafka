package com.clp.kafka.demo.customer.rest;

import com.example.eventcommons.event.customer.CustomerGroupSnapshot;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.clp.kafka.demo.customer.event.CustomerEventHandlers.CUSTOMERS_STREAM_STORE;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomerSearchService {
    private final InteractiveQueryService queryService;

    @GetMapping("/{groupId}")
    public CustomerGroupSnapshot updateGroup(@PathVariable String groupId) {
        ReadOnlyKeyValueStore<String, CustomerGroupSnapshot> keyValueStore =
                queryService.getQueryableStore(CUSTOMERS_STREAM_STORE, QueryableStoreTypes.keyValueStore());
        return keyValueStore.get(groupId);
    }
}
