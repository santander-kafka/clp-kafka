package com.clp.kafka.demo.product.domain.overdraft;

import com.clp.kafka.demo.product.domain.BaseProductData;
import com.clp.kafka.demo.product.domain.ProductType;
import com.clp.kafka.demo.product.domain.WithSnapshot;
import com.clp.kafka.demo.product.domain.behaviour.CancelBehaviour;
import com.clp.kafka.demo.product.domain.behaviour.ModifyBehaviour;
import com.clp.kafka.demo.product.domain.behaviour.RenewBehaviour;
import com.clp.kafka.demo.product.domain.behaviour.ResignBehaviour;
import com.clp.kafka.demo.product.domain.behaviour.UpdateBehaviour;
import com.example.eventcommons.event.product.form.OverdraftForm;
import com.example.eventcommons.event.product.form.ProductForm;
import com.example.eventcommons.event.product.snapshot.OverdraftSnapshot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@Getter(AccessLevel.PACKAGE)
@Setter(AccessLevel.PACKAGE)
public class Overdraft extends BaseProductData implements UpdateBehaviour, ModifyBehaviour, RenewBehaviour, ResignBehaviour, CancelBehaviour {

    private String customerId;
    private Long orderId;

    private BigDecimal amount;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDate beginDate;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDate maturityDate;

    public static Overdraft createFromForm(final OverdraftForm productForm) {
        Overdraft product = new Overdraft();
        product.productType = ProductType.valueOf(productForm.getType());
        product.customerId = productForm.getCustomerId();
        product.orderId = productForm.getOrderId();
        product.amount = productForm.getAmount();
        product.beginDate = productForm.getBeginDate();
        product.maturityDate = productForm.getMaturityDate();

        return product;
    }


    @Override
    public WithSnapshot updateFromForm(final ProductForm productForm) {
        OverdraftForm source = (OverdraftForm) productForm;

        amount = source.getAmount();
        beginDate = source.getBeginDate();
        maturityDate = source.getMaturityDate();
        return this;
    }

    @Override
    public WithSnapshot modify() {
        Overdraft copy = copy();
        //set business product number, dates...
        return copy;
    }

    @Override
    public WithSnapshot renew() {
        Overdraft copy = copy();
        //set business product number, dates...
        return copy;
    }

    @Override
    public WithSnapshot resign() {
        Overdraft copy = copy();
        //set business product number, dates...
        return copy;
    }

    @Override
    public WithSnapshot cancel() {
        // mark as canceled
        return this;
    }

    public OverdraftSnapshot snapshot() {
        return OverdraftSnapshot.builder()
                .id(getId())
                .orderId(orderId)
                .customerId(customerId)
                .amount(amount)
                .type(productType.name())
                .beginDate(beginDate)
                .maturityDate(maturityDate)
                .build();
    }

    private Overdraft copy() {
        Overdraft product = new Overdraft();
        product.customerId = getCustomerId();
        product.orderId = getOrderId();
        product.amount = getAmount();
        product.beginDate = getBeginDate();
        product.productType = getProductType();
        product.maturityDate = getMaturityDate();
        return product;
    }

    public Overdraft() {
        super(ProductType.OVERDRAFT);
    }

}
