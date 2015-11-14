package org.cmas.entities;

import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;


@Entity
@Table(name = "user_balances")
public class UserBalance implements Serializable {

    private static final long serialVersionUID = 7938270442178947557L;

    @Id
    @GeneratedValue(generator = "customForeignGenerator")
    @org.hibernate.annotations.GenericGenerator(
            name = "customForeignGenerator",
            strategy = "foreign",
            parameters = @Parameter(name = "property", value = "user")
    )
    private Long id;

    @Version
    private long version;

    @OneToOne(mappedBy = "userBalance")
    @PrimaryKeyJoinColumn
    private User user;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
}