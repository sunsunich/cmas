package org.cmas.entities.cards;

import com.google.myjson.annotations.Expose;
import org.cmas.entities.UserFile;
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
import java.util.Date;

/**
 * Created on Oct 19, 2019
 *
 * @author Alexander Petukhov
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "card_approval_requests")
public class CardApprovalRequest extends CardData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Expose
    private boolean deleted;

    @Column(nullable = false)
    private Date createDate;

    @ManyToOne
    private Diver diver;

    @OneToOne
    private UserFile frontImage;

    @OneToOne
    private UserFile backImage;

    @Expose
    @Column
    @Enumerated(EnumType.STRING)
    private CardApprovalRequestStatus status;

    public CardApprovalRequest() {
        status = CardApprovalRequestStatus.NEW;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
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

    public UserFile getFrontImage() {
        return frontImage;
    }

    public void setFrontImage(UserFile frontImage) {
        this.frontImage = frontImage;
    }

    public UserFile getBackImage() {
        return backImage;
    }

    public void setBackImage(UserFile backImage) {
        this.backImage = backImage;
    }

    public CardApprovalRequestStatus getStatus() {
        return status;
    }

    public void setStatus(CardApprovalRequestStatus status) {
        this.status = status;
    }
}
