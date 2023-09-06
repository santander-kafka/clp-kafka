package com.clp.kafka.demo.backendforfrontend.rest;

import com.clp.kafka.demo.backendforfrontend.domain.order.PricingOrderBaseDto;
import com.clp.kafka.demo.backendforfrontend.domain.product.ProductDto;
import com.clp.kafka.demo.backendforfrontend.service.BackendForFrontendFacade;
import com.clp.kafka.demo.backendforfrontend.service.PricingOrderSearchService;
import com.clp.kafka.demo.backendforfrontend.service.ProductSearchService;
import com.clp.kafka.demo.backendforfrontend.service.dto.PricingOrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/query/order")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PricingOrderController {
    private final PricingOrderSearchService pricingOrderSearchService;
    private final ProductSearchService productSearchService;
    private final BackendForFrontendFacade facade;


    @GetMapping(value = "/", params = "groupId")
    public List<PricingOrderDto> findAllByGroupId(@RequestParam String groupId) {
        return facade.findAllByGroupId(groupId);
    }

    @GetMapping(value = "/base", params = "groupId")
    public List<PricingOrderBaseDto> findAllByBaseGroupId(@RequestParam String groupId) {
        return pricingOrderSearchService.findAllByGroupId(groupId);
    }

    @GetMapping(value = "/{orderId}")
    public PricingOrderDto getById(@PathVariable Long orderId) {
        return facade.getById(orderId);
    }

    @GetMapping(value = "/{orderId}/product")
    public List<ProductDto> findAllByOrderId(@PathVariable Long orderId) {
        return productSearchService.findAllByOrderId(orderId);
    }

    @GetMapping(value = "/{orderId}/product/{productId}")
    public ProductDto getProductById(@PathVariable Long productId) {
        return productSearchService.getById(productId);
    }

}
