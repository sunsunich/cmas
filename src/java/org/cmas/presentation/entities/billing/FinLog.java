package org.cmas.presentation.entities.billing;

import org.cmas.presentation.entities.user.UserClient;
import org.cmas.Globals;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "fin_log")
public class FinLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "validation.emptyField")
    @Column(nullable = false)
    private Date recordDate;

    @ManyToOne
    private UserClient user;

    @Column(nullable=false, precision = Globals.PRECISION, scale = Globals.SCALE)
    private BigDecimal amount;

    @Column(nullable = false)
	@Enumerated(EnumType.STRING)
    private OperationType operationType;

    private String description;

    private String ip;

    protected FinLog() {
    }

    public FinLog(UserClient user, BigDecimal amount, OperationType operationType, String ip) {
        recordDate = new Date();
        this.user = user;
        this.amount = amount;
        this.operationType = operationType;
        this.ip = ip;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    public UserClient getUser() {
        return user;
    }

    public void setUser(UserClient user) {
        this.user = user;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
