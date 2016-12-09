package org.cmas.presentation.entities.billing;

import org.cmas.Globals;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "fin_settings")
public class FinSettings {

    /*
    userService.defaultDiscountPercent = 0
billingService.commissionPercent = 15
billingService.orderLimitDollar = 1000
billingService.shippingFirstDollar = 100
billingService.shippingRestDollar = 100
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, precision = Globals.PRECISION, scale = Globals.SCALE)
    private BigDecimal defaultDiscountPercent;

    @Column(nullable=false, precision = Globals.PRECISION, scale = Globals.SCALE)
    private BigDecimal commissionPercent;

    @Column(nullable=false, precision = Globals.PRECISION, scale = Globals.SCALE)
    private BigDecimal orderLimitDollar;

    @Column(nullable=false, precision = Globals.PRECISION, scale = Globals.SCALE)
    private BigDecimal shippingFirstDollar;

    @Column(nullable=false, precision = Globals.PRECISION, scale = Globals.SCALE)
    private BigDecimal shippingRestDollar;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getDefaultDiscountPercent() {
        return defaultDiscountPercent;
    }

    public void setDefaultDiscountPercent(BigDecimal defaultDiscountPercent) {
        this.defaultDiscountPercent = defaultDiscountPercent;
    }

    public BigDecimal getCommissionPercent() {
        return commissionPercent;
    }

    public void setCommissionPercent(BigDecimal commissionPercent) {
        this.commissionPercent = commissionPercent;
    }

    public BigDecimal getOrderLimitDollar() {
        return orderLimitDollar;
    }

    public void setOrderLimitDollar(BigDecimal orderLimitDollar) {
        this.orderLimitDollar = orderLimitDollar;
    }

    public BigDecimal getShippingFirstDollar() {
        return shippingFirstDollar;
    }

    public void setShippingFirstDollar(BigDecimal shippingFirstDollar) {
        this.shippingFirstDollar = shippingFirstDollar;
    }

    public BigDecimal getShippingRestDollar() {
        return shippingRestDollar;
    }

    public void setShippingRestDollar(BigDecimal shippingRestDollar) {
        this.shippingRestDollar = shippingRestDollar;
    }
}
