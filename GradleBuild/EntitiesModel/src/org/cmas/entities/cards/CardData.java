package org.cmas.entities.cards;

import com.google.myjson.annotations.Expose;
import org.cmas.entities.diver.DiverLevel;
import org.cmas.entities.diver.DiverType;
import org.cmas.entities.sport.NationalFederation;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
 * Created on Oct 19, 2019
 *
 * @author Alexander Petukhov
 */
@MappedSuperclass
public class CardData {

    @Expose
    @Column
    @Enumerated(EnumType.STRING)
    private DiverType diverType;

    @Expose
    @Column
    @Enumerated(EnumType.STRING)
    private DiverLevel diverLevel;

    @Expose
    @Column
    @Enumerated(EnumType.STRING)
    private PersonalCardType cardType;

    @Expose
    @Column
    private String federationName;

    @Expose
    @Column
    private Date validUntil;

    @ManyToOne
    private NationalFederation issuingFederation;

    public NationalFederation getIssuingFederation() {
        return issuingFederation;
    }

    public void setIssuingFederation(NationalFederation issuingFederation) {
        this.issuingFederation = issuingFederation;
    }

    public Date getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Date validUntil) {
        this.validUntil = validUntil;
    }

    public DiverType getDiverType() {
        return diverType;
    }

    public void setDiverType(DiverType diverType) {
        this.diverType = diverType;
    }

    public String getFederationName() {
        return federationName;
    }

    public void setFederationName(String federationName) {
        this.federationName = federationName;
    }

    public PersonalCardType getCardType() {
        return cardType;
    }

    public void setCardType(PersonalCardType cardType) {
        this.cardType = cardType;
    }


    public DiverLevel getDiverLevel() {
        return diverLevel;
    }

    public void setDiverLevel(DiverLevel diverLevel) {
        this.diverLevel = diverLevel;
    }


}
