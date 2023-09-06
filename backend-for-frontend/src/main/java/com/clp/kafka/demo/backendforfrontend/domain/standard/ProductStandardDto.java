package com.clp.kafka.demo.backendforfrontend.domain.standard;

import com.example.eventcommons.event.productstandard.ProductStandardSnapshot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductStandardDto {
    @Id
    private Long id;
    private boolean standard;

    public static ProductStandardDto from(ProductStandardSnapshot snapshot) {
        ProductStandardDto result = new ProductStandardDto();
        result.id = snapshot.getId();
        result.standard = snapshot.isStandard();
        return result;
    }
}
