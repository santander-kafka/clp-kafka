package com.clp.kafka.demo.product.domain.guaranteelimit;

import com.clp.kafka.demo.product.domain.BaseProductData;
import com.clp.kafka.demo.product.domain.ProductType;
import com.clp.kafka.demo.product.domain.WithSnapshot;
import com.clp.kafka.demo.product.domain.behaviour.CancelBehaviour;
import com.clp.kafka.demo.product.domain.behaviour.ModifyBehaviour;
import com.clp.kafka.demo.product.domain.behaviour.RenewBehaviour;
import com.clp.kafka.demo.product.domain.behaviour.ResignBehaviour;
import com.clp.kafka.demo.product.domain.behaviour.UpdateBehaviour;
import com.example.eventcommons.event.product.form.GuaranteeLimitForm;
import com.example.eventcommons.event.product.form.ProductForm;
import com.example.eventcommons.event.product.snapshot.GuaranteeLimitSnapshot;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@Getter(AccessLevel.PACKAGE)
@Setter(AccessLevel.PACKAGE)
public class GuaranteeLimit extends BaseProductData implements UpdateBehaviour, ModifyBehaviour, RenewBehaviour, ResignBehaviour, CancelBehaviour {
    private String customerId;
    private Long orderId;
    private BigDecimal amount;

    private Integer commission;
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDate beginDate;
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDate maturityDate;
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Long> absorbedProductsIds = new HashSet<>();

    public static GuaranteeLimit createFromForm(final GuaranteeLimitForm productForm) {
        GuaranteeLimit product = new GuaranteeLimit();
        product.productType = ProductType.valueOf(productForm.getType());
        product.customerId = productForm.getCustomerId();
        product.orderId = productForm.getOrderId();
        product.amount = productForm.getAmount();
        product.commission = productForm.getCommission();
        product.beginDate = productForm.getBeginDate();
        product.maturityDate = productForm.getMaturityDate();
        product.absorbedProductsIds.addAll(productForm.getAbsorbedProductIds());

        return product;
    }

    @Override
    public WithSnapshot updateFromForm(ProductForm productForm) {
        GuaranteeLimitForm source = (GuaranteeLimitForm) productForm;

        amount = source.getAmount();
        beginDate = source.getBeginDate();
        maturityDate = source.getMaturityDate();
        commission = source.getCommission();
        absorbedProductsIds.clear();
        absorbedProductsIds.addAll(source.getAbsorbedProductIds());
        return this;
    }

    @Override
    public WithSnapshot modify() {
        GuaranteeLimit copy = copy();
        //set business product number, dates...
        return copy;
    }

    @Override
    public WithSnapshot renew() {
        GuaranteeLimit copy = copy();
        //set business product number, dates...
        return copy;
    }

    @Override
    public WithSnapshot resign() {
        GuaranteeLimit copy = copy();
        //set business product number, dates...
        return copy;
    }

    @Override
    public WithSnapshot cancel() {
        // mark as canceled
        return this;
    }

    public GuaranteeLimitSnapshot snapshot() {
        return GuaranteeLimitSnapshot.builder()
                .id(getId())
                .orderId(orderId)
                .customerId(customerId)
                .amount(amount)
                .commission(commission)
                .beginDate(beginDate)
                .maturityDate(maturityDate)
                .type(productType.name())
                .absorbedProductIds(absorbedProductsIds)
                .build();
    }

    private GuaranteeLimit copy() {
        GuaranteeLimit product = new GuaranteeLimit();
        product.customerId = getCustomerId();
        product.orderId = getOrderId();
        product.amount = getAmount();
        product.beginDate = getBeginDate();
        product.maturityDate = getMaturityDate();
        return product;
    }

    public GuaranteeLimit() {
        super(ProductType.GUARANTEE_LIMIT);
    }

}

