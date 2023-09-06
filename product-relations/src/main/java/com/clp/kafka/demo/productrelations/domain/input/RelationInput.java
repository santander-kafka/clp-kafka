package com.clp.kafka.demo.productrelations.domain.input;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RelationInput {
    InputRelationType type;
    Long sourceId;
    Long targetId;
}
