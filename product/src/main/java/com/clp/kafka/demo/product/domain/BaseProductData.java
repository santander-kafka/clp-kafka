package com.clp.kafka.demo.product.domain;

import com.example.eventcommons.event.product.snapshot.ProductSnapshot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class BaseProductData implements WithSnapshot {
    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    protected ProductType productType;

    public BaseProductData(final ProductType productType) {
        this.productType = productType;
    }

    @Override
    public abstract <T extends ProductSnapshot> T snapshot();
}
