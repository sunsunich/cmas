package org.cmas.entities.loyalty;

import org.cmas.Globals;
import org.cmas.entities.Address;
import org.cmas.entities.Gender;
import org.cmas.entities.billing.Invoice;
import org.cmas.entities.diver.Diver;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created on May 28, 2019
 *
 * @author Alexander Petukhov
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "insurance_requests")
public class InsuranceRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Date createDate;

    @ManyToOne
    private Diver diver;

    @ManyToOne
    private Address address;

    @Column
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(precision = Globals.PRECISION, scale = Globals.SCALE)
    private BigDecimal insurancePrice;

    @OneToOne
    private Invoice invoice;

    @Column
    private String response;

    @Column
    private boolean sent;

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
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

    public Diver getDiver() {
        return diver;
    }

    public void setDiver(Diver diver) {
        this.diver = diver;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Gender getGender() {
        return gender;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public BigDecimal getInsurancePrice() {
        return insurancePrice;
    }

    public void setInsurancePrice(BigDecimal insurancePrice) {
        this.insurancePrice = insurancePrice;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }
}
