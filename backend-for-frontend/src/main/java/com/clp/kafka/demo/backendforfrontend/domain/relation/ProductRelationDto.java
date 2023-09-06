package com.clp.kafka.demo.backendforfrontend.domain.relation;

import com.example.eventcommons.event.relations.ProductRelationSnapshot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRelationDto {
    @Id
    private Long id;
    private Long sourceId;
    private Long targetId;
    private boolean active;
    private boolean archival;

    public static ProductRelationDto from(ProductRelationSnapshot snaphot) {
        ProductRelationDto result = new ProductRelationDto();
        result.id = snaphot.getId();
        result.sourceId = snaphot.getSourceId();
        result.targetId = snaphot.getTargetId();
        result.active = snaphot.isActive();
        result.archival = snaphot.isArchival();
        return result;
    }
}
