package com.clp.kafka.demo.backendforfrontend.domain.order;

import com.example.eventcommons.event.order.PricingOrderSnapshot;
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
public class PricingOrderBaseDto {
    @Id
    private Long id;
    private String groupId;

    public static PricingOrderBaseDto from(PricingOrderSnapshot snapshot) {
        PricingOrderBaseDto dto = new PricingOrderBaseDto();
        dto.id = snapshot.getId();
        dto.groupId = snapshot.getGroupId();
        return dto;
    }
}
