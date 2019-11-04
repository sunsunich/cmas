package org.cmas.presentation.model.cards;

import org.cmas.Globals;
import org.cmas.entities.cards.PersonalCardType;
import org.cmas.entities.diver.DiverLevel;
import org.cmas.presentation.validator.Validatable;
import org.cmas.presentation.validator.ValidatorUtils;
import org.hibernate.validator.NotEmpty;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created on Sep 30, 2019
 *
 * @author Alexander Petukhov
 */
public class CardApprovalRequestFormObject implements Validatable {

//    @NotEmpty(message = "validation.emptyField")
    private String diverType;

    @NotEmpty(message = "validation.emptyField")
    private String diverLevel;

  //  @NotEmpty(message = "validation.emptyField")
    private String cardType;

    private String validUntil;

    private String federationCardNumber;

    @NotEmpty(message = "validation.emptyField")
    private String countryCode;

    private String federationId;

    private MultipartFile frontImage;

    private MultipartFile backImage;

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
                                    DiverLevel.class,
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

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getFederationId() {
        return federationId;
    }

    public void setFederationId(String federationId) {
        this.federationId = federationId;
    }

    public MultipartFile getFrontImage() {
        return frontImage;
    }

    public void setFrontImage(MultipartFile frontImage) {
        this.frontImage = frontImage;
    }

    public MultipartFile getBackImage() {
        return backImage;
    }

    public void setBackImage(MultipartFile backImage) {
        this.backImage = backImage;
    }
}
