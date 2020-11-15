package org.cmas.entities.loyalty;

import org.cmas.Globals;
import org.cmas.entities.UserAwareEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;


@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "camera_orders")
public class CameraOrder extends UserAwareEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Date createDate;

    @Column(nullable = false)
    private String cameraName;

    @Column(nullable = false)
    private String sendToEmail;

    @Column(nullable=false, precision = Globals.PRECISION, scale = Globals.SCALE)
    private BigDecimal marketPriceEuro;

    @Column(nullable=false, precision = Globals.PRECISION, scale = Globals.SCALE)
    private BigDecimal discountPriceEuro;

    private String externalNumber;

    @ManyToOne
    private LoyaltyProgramItem loyaltyProgramItem;

    public CameraOrder() {
        createDate = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    public String getSendToEmail() {
        return sendToEmail;
    }

    public void setSendToEmail(String sendToEmail) {
        this.sendToEmail = sendToEmail;
    }

    public BigDecimal getMarketPriceEuro() {
        return marketPriceEuro;
    }

    public void setMarketPriceEuro(BigDecimal marketPrice) {
        this.marketPriceEuro = marketPrice;
    }

    public BigDecimal getDiscountPriceEuro() {
        return discountPriceEuro;
    }

    public void setDiscountPriceEuro(BigDecimal discountPrice) {
        this.discountPriceEuro = discountPrice;
    }

    public String getExternalNumber() {
        return externalNumber;
    }

    public void setExternalNumber(String externalNumber) {
        this.externalNumber = externalNumber;
    }

    public LoyaltyProgramItem getLoyaltyProgramItem() {
        return loyaltyProgramItem;
    }

    public void setLoyaltyProgramItem(LoyaltyProgramItem loyaltyProgramItem) {
        this.loyaltyProgramItem = loyaltyProgramItem;
    }
}
