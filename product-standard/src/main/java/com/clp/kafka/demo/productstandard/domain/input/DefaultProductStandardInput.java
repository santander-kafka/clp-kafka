package com.clp.kafka.demo.productstandard.domain.input;


import com.example.eventcommons.event.product.snapshot.GuaranteeLimitSnapshot;
import com.example.eventcommons.event.product.snapshot.GuaranteeProductSnapshot;
import com.example.eventcommons.event.product.snapshot.ProductSnapshot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DefaultProductStandardInput implements ProductStandardInput {
    private Long productId = null;
    private Integer commission = null;

    public static DefaultProductStandardInput from(ProductSnapshot snapshot) {
        return DefaultProductStandardInput.getForm(snapshot);
    }

    public static DefaultProductStandardInput from(ProductSnapshot targetSnapshot, ProductSnapshot sourceSnapshot) {
        return DefaultProductStandardInput.getForm(targetSnapshot);
    }

    static DefaultProductStandardInput getForm(ProductSnapshot snapshot) {
        return switch (snapshot.getType()) {
            case "GUARANTEE_LIMIT" -> DefaultProductStandardInput.builder()
                    .productId(snapshot.getId())
                    .commission(((GuaranteeLimitSnapshot) snapshot).getCommission())
                    .build();
            case "GUARANTEE_PRODUCT" -> DefaultProductStandardInput.builder()
                    .productId(snapshot.getId())
                    .commission(((GuaranteeProductSnapshot) snapshot).getCommission())
                    .build();
            default -> DefaultProductStandardInput.builder()
                    .productId(snapshot.getId())
                    .build();
        };
    }


}
