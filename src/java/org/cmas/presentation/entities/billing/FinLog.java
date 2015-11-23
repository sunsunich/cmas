package org.cmas.presentation.entities.billing;

import org.cmas.Globals;
import org.cmas.entities.amateur.Amateur;
import org.cmas.entities.sport.Sportsman;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
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

    @ManyToOne(optional = true, fetch = FetchType.LAZY, targetEntity = Sportsman.class)
    private Sportsman sportsman;

    @ManyToOne(optional = true, fetch = FetchType.LAZY, targetEntity = Amateur.class)
    private Amateur amateur;

    @Column(nullable=false, precision = Globals.PRECISION, scale = Globals.SCALE)
    private BigDecimal amount;

    @Column(nullable = false)
	@Enumerated(EnumType.STRING)
    private OperationType operationType;

    private String description;

    private String ip;

    protected FinLog() {
    }

    public FinLog(Sportsman sportsman, BigDecimal amount, OperationType operationType, String ip) {
        recordDate = new Date();
        this.sportsman = sportsman;
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

    public Sportsman getSportsman() {
        return sportsman;
    }

    public void setSportsman(Sportsman sportsman) {
        this.sportsman = sportsman;
    }

    public Amateur getAmateur() {
        return amateur;
    }

    public void setAmateur(Amateur amateur) {
        this.amateur = amateur;
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
