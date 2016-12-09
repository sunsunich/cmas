package org.cmas.presentation.entities.billing;

import org.cmas.Globals;
import org.cmas.entities.UserAwareEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import java.math.BigDecimal;
import java.util.Date;


@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name="invoice")
public class Invoice extends UserAwareEntity implements Comparable<Invoice>{
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private long version;

    // тип счета.
    @Column
    @Enumerated(EnumType.STRING)
    private InvoiceType invoiceType;

    // Скока денег
    @Column(nullable=false, precision = Globals.PRECISION, scale = Globals.SCALE)
    private BigDecimal amount;

    // Дата создания документа
    @Column(nullable=false)
    private Date createDate;

    @Column(nullable=true)
    private Date transactionDate;

    // Статус счёта: оплачен, частично оплачен, не оплачен
    @Column(columnDefinition = "int(11) NOT NULL DEFAULT '0'")
    private InvoiceStatus invoiceStatus;

    @Column
    private String description;

    /**
     * счета удалять нельзя, ибо бухгалтения против.
     */
    @Column(columnDefinition = "tinyint(1) NOT NULL DEFAULT '0'")
    private boolean deleted;

    private String externalInvoiceNumber;

    public Invoice() {
        invoiceStatus = InvoiceStatus.NOT_PAID;
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

    public String getExternalInvoiceNumber() {
        return externalInvoiceNumber;
    }

    public void setExternalInvoiceNumber(String externalInvoiceNumber) {
        this.externalInvoiceNumber = externalInvoiceNumber;
    }

    public InvoiceStatus getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(InvoiceStatus invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public InvoiceType getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(InvoiceType invoiceType) {
        this.invoiceType = invoiceType;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    @SuppressWarnings({"CallToSimpleGetterFromWithinClass"})
    @Override
    public boolean equals(Object obj) {
        if (this == obj){
            return true;
        }
        if (obj == null || getClass() != obj.getClass()){
            return false;
        }

        Invoice invoice = (Invoice) obj;

        //noinspection RedundantIfStatement
        if (getId() != null ? !getId().equals(invoice.getId()) : invoice.getId() != null){
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @SuppressWarnings({"CallToSimpleGetterFromWithinClass"})
    @Override
    public int compareTo(Invoice o) {
        int result = getCreateDate().compareTo(o.getCreateDate());
        if (result == 0) {
            //noinspection SubtractionInCompareTo
            return getId() > o.getId() ? 1 : (getId() < o.getId() ? -1 : 0);
        } else {
            return result;
        }
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }


    @Override
    public String toString() {
        return "Invoice{" +
                "id=" + id +
                ", createDate=" + createDate +
                ", invoiceType=" + invoiceType +
                ", invoiceStatus=" + invoiceStatus +
                '}';
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
