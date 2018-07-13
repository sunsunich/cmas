package org.cmas.presentation.model.registration;

import com.google.myjson.annotations.Expose;

/**
 * Created on Jun 18, 2018
 *
 * @author Alexander Petukhov
 */
public class DiverRegistrationDTO {

    @Expose
    private Long diverId;

    @Expose
    private String firstName;

    @Expose
    private String lastName;

    @Expose
    private String maskedEmail;

    @Expose
    private String cardNumber;

    @Expose
    private String cardImageUrl;

    public Long getDiverId() {
        return diverId;
    }

    public void setDiverId(Long diverId) {
        this.diverId = diverId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMaskedEmail() {
        return maskedEmail;
    }

    public void setMaskedEmail(String maskedEmail) {
        this.maskedEmail = maskedEmail;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardImageUrl() {
        return cardImageUrl;
    }

    public void setCardImageUrl(String cardImageUrl) {
        this.cardImageUrl = cardImageUrl;
    }
}
