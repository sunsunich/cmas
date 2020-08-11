package org.cmas.presentation.model.cards;

import com.google.myjson.annotations.Expose;
import org.cmas.Globals;
import org.cmas.entities.cards.PersonalCardType;
import org.cmas.entities.diver.DiverLevel;
import org.cmas.entities.diver.DiverType;
import org.cmas.presentation.validator.Validatable;
import org.cmas.presentation.validator.ValidatorUtils;
import org.hibernate.validator.NotEmpty;
import org.springframework.validation.Errors;

public class CommonCardApprovalRequestFormObject implements Validatable {

    @Expose
    //    @NotEmpty(message = "validation.emptyField")
    private String diverType;

    @Expose
    @NotEmpty(message = "validation.emptyField")
    private String diverLevel;

    @Expose
    //  @NotEmpty(message = "validation.emptyField")
    private String cardType;

    @Expose
    private String validUntil;

    @Expose
    private String federationCardNumber;

    @Expose
    private String federationId;

    @Override
    public void validate(Errors errors) {
        ValidatorUtils.validateDate(errors,
                                    validUntil,
                                    "validUntil",
                                    "validation.incorrectDate",
                                    Globals.getDTF());
        ValidatorUtils.validateEnum(errors,
                                    diverLevel,
                                    DiverLevel.class,
                                    "diverLevel",
                                    "validation.incorrectField");
        ValidatorUtils.validateEnum(errors,
                                    diverType,
                                    DiverType.class,
                                    "diverType",
                                    "validation.incorrectField");
        ValidatorUtils.validateEnum(errors,
                                    cardType,
                                    PersonalCardType.class,
                                    "cardType",
                                    "validation.incorrectField");
        ValidatorUtils.validateLong(errors,
                                    federationId,
                                    "federationId",
                                    "validation.incorrectField");
    }

    public String getDiverType() {
        return diverType;
    }

    public void setDiverType(String diverType) {
        this.diverType = diverType;
    }

    public String getDiverLevel() {
        return diverLevel;
    }

    public void setDiverLevel(String diverLevel) {
        this.diverLevel = diverLevel;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(String validUntil) {
        this.validUntil = validUntil;
    }

    public String getFederationCardNumber() {
        return federationCardNumber;
    }

    public void setFederationCardNumber(String federationCardNumber) {
        this.federationCardNumber = federationCardNumber;
    }

    public String getFederationId() {
        return federationId;
    }

    public void setFederationId(String federationId) {
        this.federationId = federationId;
    }
}
