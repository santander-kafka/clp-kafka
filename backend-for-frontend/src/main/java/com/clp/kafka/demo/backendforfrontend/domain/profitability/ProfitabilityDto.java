package com.clp.kafka.demo.backendforfrontend.domain.profitability;


import com.example.eventcommons.event.profitability.event.ProfitabilitySnapshot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfitabilityDto {
    @Id
    private String id;
    private Long orderId;
    private List<String> data = new ArrayList<>();

    static ProfitabilityDto from(ProfitabilitySnapshot snapshot) {
        return ProfitabilityDto.builder()
                .orderId(snapshot.getOrderId())
                .data(new ArrayList<>(snapshot.getPricings()))
                .build();
    }
}
