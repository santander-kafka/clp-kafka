package com.clp.kafka.demo.backendforfrontend.domain.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto implements Serializable {
    @Id
    private String id;
    private String customerId;
    private String name;

}
