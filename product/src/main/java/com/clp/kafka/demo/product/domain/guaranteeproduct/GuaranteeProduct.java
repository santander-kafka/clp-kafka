package com.clp.kafka.demo.product.domain.guaranteeproduct;

import com.clp.kafka.demo.product.domain.BaseProductData;
import com.clp.kafka.demo.product.domain.ProductType;
import com.clp.kafka.demo.product.domain.WithSnapshot;
import com.clp.kafka.demo.product.domain.behaviour.CancelBehaviour;
import com.clp.kafka.demo.product.domain.behaviour.ModifyBehaviour;
import com.clp.kafka.demo.product.domain.behaviour.RenewBehaviour;
import com.clp.kafka.demo.product.domain.behaviour.ResignBehaviour;
import com.clp.kafka.demo.product.domain.behaviour.UpdateBehaviour;
import com.example.eventcommons.event.product.form.GuaranteeProductForm;
import com.example.eventcommons.event.product.form.ProductForm;
import com.example.eventcommons.event.product.snapshot.GuaranteeProductSnapshot;
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
@Getter(AccessLevel.PROTECTED)
@Setter(AccessLevel.PACKAGE)
@AllArgsConstructor
public class GuaranteeProduct extends BaseProductData implements UpdateBehaviour, ModifyBehaviour, RenewBehaviour, ResignBehaviour, CancelBehaviour {
    private String customerId;
    private Long orderId;
    private BigDecimal amount;
    private Integer commission;
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDate beginDate;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDate maturityDate;

    public static GuaranteeProduct createFromForm(final GuaranteeProductForm productForm) {
        GuaranteeProduct product = new GuaranteeProduct();
        product.customerId = productForm.getCustomerId();
        product.orderId = productForm.getOrderId();
        product.amount = productForm.getAmount();
        product.commission = productForm.getCommission();
        product.beginDate = productForm.getBeginDate();
        product.maturityDate = productForm.getMaturityDate();
        product.productType = ProductType.valueOf(productForm.getType());

        return product;
    }

    @Override
    public WithSnapshot updateFromForm(ProductForm productForm) {
        GuaranteeProductForm source = (GuaranteeProductForm) productForm;

        amount = source.getAmount();
        commission = source.getCommission();
        beginDate = source.getBeginDate();
        maturityDate = source.getMaturityDate();
        return this;
    }

    @Override
    public WithSnapshot modify() {
        GuaranteeProduct copy = copy();
        //set business product number, dates...
        return copy;
    }

    @Override
    public WithSnapshot renew() {
        GuaranteeProduct copy = copy();
        //set business product number, dates...
        return copy;
    }

    @Override
    public WithSnapshot resign() {
        GuaranteeProduct copy = copy();
        //set business product number, dates...
        return copy;
    }

    @Override
    public WithSnapshot cancel() {
        // mark as canceled
        return this;
    }

    public GuaranteeProductSnapshot snapshot() {
        return GuaranteeProductSnapshot.builder()
                .id(getId())
                .orderId(orderId)
                .customerId(customerId)
                .amount(amount)
                .commission(commission)
                .type(productType.name())
                .beginDate(beginDate)
                .maturityDate(maturityDate)
                .build();
    }


    private GuaranteeProduct copy() {
        GuaranteeProduct product = new GuaranteeProduct();
        product.customerId = getCustomerId();
        product.orderId = getOrderId();
        product.amount = getAmount();
        product.beginDate = getBeginDate();
        product.productType = getProductType();
        product.maturityDate = getMaturityDate();
        return product;
    }

    public GuaranteeProduct() {
        super(ProductType.GUARANTEE_PRODUCT);
    }
}
