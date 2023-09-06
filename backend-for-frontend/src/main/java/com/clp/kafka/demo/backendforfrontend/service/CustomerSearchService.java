package com.clp.kafka.demo.backendforfrontend.service;

import com.clp.kafka.demo.backendforfrontend.domain.customer.CustomerDomainRepository;
import com.clp.kafka.demo.backendforfrontend.domain.customer.CustomerGroupDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/query/customer")
public class CustomerSearchService {
    private final CustomerDomainRepository repository;

    @GetMapping("/{groupId}")
    public CustomerGroupDto getByGroupId(@PathVariable String groupId) {
        return repository.getByGroupId(groupId);
    }
}
