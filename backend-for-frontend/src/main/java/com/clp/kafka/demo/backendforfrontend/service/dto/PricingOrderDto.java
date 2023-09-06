package com.clp.kafka.demo.backendforfrontend.service.dto;

import com.clp.kafka.demo.backendforfrontend.domain.customer.CustomerGroupDto;
import com.clp.kafka.demo.backendforfrontend.domain.decision.DecisionDto;
import com.clp.kafka.demo.backendforfrontend.domain.product.ProductDto;
import com.clp.kafka.demo.backendforfrontend.domain.profitability.ProfitabilityDto;
import com.clp.kafka.demo.backendforfrontend.domain.relation.ProductRelationDto;
import com.clp.kafka.demo.backendforfrontend.domain.standard.ProductStandardDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PricingOrderDto {
    private String groupId;
    private CustomerGroupDto customers;
    private List<ProductDto> products;
    private ProfitabilityDto profitabilityDto;
    private List<ProductRelationDto> productRelations;
    private List<ProductStandardDto> productStandards;
    private DecisionDto decision;
}
