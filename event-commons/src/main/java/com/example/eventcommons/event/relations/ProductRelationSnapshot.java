package com.example.eventcommons.event.relations;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ProductRelationSnapshot {
    private Long id;
    private Long sourceId;
    private Long targetId;
    private boolean active;
    private boolean archival;
}
