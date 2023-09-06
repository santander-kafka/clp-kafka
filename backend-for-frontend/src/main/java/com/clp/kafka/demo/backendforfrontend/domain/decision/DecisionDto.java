package com.clp.kafka.demo.backendforfrontend.domain.decision;

import com.example.eventcommons.event.decision.event.DecisionSnapshot;
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
public class DecisionDto {
    @Id
    private String id;
    private Long orderId;
    private String result;
    @Builder.Default
    private Boolean archival = false;
    @Builder.Default
    private List<String> errors = new ArrayList<>();

    static DecisionDto from(DecisionSnapshot snapshot) {
        return DecisionDto.builder()
                .orderId(snapshot.getOrderId())
                .errors(snapshot.getErrors())
                .result(snapshot.getResult())
                .build();
    }

    public static DecisionDto archive(DecisionSnapshot snapshot) {
        return DecisionDto.builder()
                .orderId(snapshot.getOrderId())
                .result(snapshot.getResult())
                .archival(true)
                .build();
    }
}
