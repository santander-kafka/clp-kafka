package com.clp.kafka.demo.backendforfrontend.domain.product;

import com.example.eventcommons.event.product.snapshot.ProductSnapshot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    @Id
    private Long id;
    private String customerId;
    private Long orderId;
    private ProductSnapshot snapshot;

    public static ProductDto from(ProductSnapshot snapshot) {
        ProductDto dto = new ProductDto();
        dto.id = snapshot.getId();
        dto.customerId = snapshot.getCustomerId();
        dto.orderId = snapshot.getOrderId();
        dto.snapshot = snapshot;
        return dto;
    }
}
