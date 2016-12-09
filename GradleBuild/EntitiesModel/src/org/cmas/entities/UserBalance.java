package org.cmas.entities;

import org.cmas.entities.amateur.Amateur;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.sport.Athlete;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import java.io.Serializable;
import java.math.BigDecimal;


@Entity
@Table(name = "user_balances")
public class UserBalance implements Serializable {

    private static final long serialVersionUID = 7938270442178947557L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private long version;

    @OneToOne(mappedBy = "userBalance", optional = true, fetch = FetchType.LAZY, targetEntity = Athlete.class)
    private Athlete athlete;

    @OneToOne(mappedBy = "userBalance", optional = true, fetch = FetchType.LAZY, targetEntity = Amateur.class)
    private Amateur amateur;

    @OneToOne(mappedBy = "userBalance", optional = true, fetch = FetchType.LAZY, targetEntity = Diver.class)
    private Diver diver;

    @Column
    private BigDecimal balance;

    @Column
    private BigDecimal discountPercent;

    public UserBalance() {
        balance = BigDecimal.ZERO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public Athlete getAthlete() {
        return athlete;
    }

    public void setAthlete(Athlete athlete) {
        this.athlete = athlete;
    }

    public Amateur getAmateur() {
        return amateur;
    }

    public void setAmateur(Amateur amateur) {
        this.amateur = amateur;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(BigDecimal discountPercent) {
        this.discountPercent = discountPercent;
    }

    public Diver getDiver() {
        return diver;
    }

    public void setDiver(Diver diver) {
        this.diver = diver;
    }
}