package com.clp.kafka.demo.backendforfrontend.service;

import com.clp.kafka.demo.backendforfrontend.domain.customer.CustomerGroupDto;
import com.clp.kafka.demo.backendforfrontend.domain.decision.DecisionDto;
import com.clp.kafka.demo.backendforfrontend.domain.order.PricingOrderBaseDto;
import com.clp.kafka.demo.backendforfrontend.domain.product.ProductDto;
import com.clp.kafka.demo.backendforfrontend.domain.profitability.ProfitabilityDto;
import com.clp.kafka.demo.backendforfrontend.domain.relation.ProductRelationDto;
import com.clp.kafka.demo.backendforfrontend.domain.standard.ProductStandardDto;
import com.clp.kafka.demo.backendforfrontend.service.dto.PricingOrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BackendForFrontendFacade {
    private final CustomerSearchService customerSearchService;
    private final PricingOrderSearchService pricingOrderSearchService;
    private final ProductSearchService productSearchService;
    private final ProfitabilitSerachService profitabilitSerachService;
    private final ProductRelationSearchService relationSearchService;
    private final ProductStandardSearchService standardSearchService;

    private final DecisionSearchService decisionSearchService;

    public List<PricingOrderDto> findAllByGroupId(String groupId) {
        CustomerGroupDto customers = customerSearchService.getByGroupId(groupId);
        List<PricingOrderBaseDto> orders = pricingOrderSearchService.findAllByGroupId(groupId);
        List<ProductDto> products = getAllProducts(orders);
        List<ProfitabilityDto> profitabilities = getAllProfitabilities(orders);
        List<ProductRelationDto> productRelations = getAllRelations(products);
        List<ProductStandardDto> productStandards = getAllStandards(products);
        List<DecisionDto> decisions = getAllDecisions(orders);

        return orders.stream().map(orderBase -> PricingOrderDto.builder()
                .groupId(groupId)
                .customers(customers)
                .profitabilityDto(profitabilities.stream()
                        .filter(p -> Objects.equals(p.getOrderId(), orderBase.getId()))
                        .findAny().orElse(null))
                .products(products.stream()
                        .filter(product -> Objects.equals(orderBase.getId(), product.getOrderId()))
                        .collect(Collectors.toList()))
                .productRelations(productRelations)
                .productStandards(productStandards)
                .decision(decisions.stream()
                        .filter(decision -> decision.getOrderId().equals(orderBase.getId()))
                        .findAny().orElse(null))
                .build()
        ).collect(Collectors.toList());
    }

    public PricingOrderDto getById(Long orderId) {
        PricingOrderBaseDto order = pricingOrderSearchService.getById(orderId);
        String groupId = order.getGroupId();
        CustomerGroupDto customers = customerSearchService.getByGroupId(groupId);
        ProfitabilityDto profitability = profitabilitSerachService.getByOrderId(orderId);
        List<ProductDto> products = productSearchService.findAllByOrderId(orderId);
        List<ProductRelationDto> productRelations = getAllRelations(products);
        List<ProductStandardDto> productStandards = getAllStandards(products);
        DecisionDto decision = decisionSearchService.findByOrderId(orderId);

        return PricingOrderDto.builder()
                .groupId(groupId)
                .customers(customers)
                .profitabilityDto(profitability)
                .products(products)
                .productRelations(productRelations)
                .productStandards(productStandards)
                .decision(decision)
                .build();
    }

    private List<ProductDto> getAllProducts(List<PricingOrderBaseDto> orders) {
        return productSearchService.findAllByOrderIdIn(orders.stream()
                .map(PricingOrderBaseDto::getId)
                .collect(Collectors.toList()));
    }

    private List<ProfitabilityDto> getAllProfitabilities(List<PricingOrderBaseDto> orders) {
        return profitabilitSerachService.findAllByOrderIdIn(orders.stream()
                .map(PricingOrderBaseDto::getId)
                .collect(Collectors.toList()));
    }

    private List<ProductRelationDto> getAllRelations(List<ProductDto> products) {
        return relationSearchService.getAllBySourceOrTargetIdIn(products.stream()
                .map(ProductDto::getId)
                .collect(Collectors.toList()));
    }

    private List<ProductStandardDto> getAllStandards(List<ProductDto> products) {
        return standardSearchService.findAllByProductIdIn(products.stream()
                .map(ProductDto::getId)
                .collect(Collectors.toList()));
    }

    private List<DecisionDto> getAllDecisions(List<PricingOrderBaseDto> orders) {
        return decisionSearchService.findAllByOrderIdIn(orders.stream()
                .map(PricingOrderBaseDto::getId)
                .toList());
    }

}
