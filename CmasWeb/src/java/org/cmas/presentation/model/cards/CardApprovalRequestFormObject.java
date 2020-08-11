package org.cmas.presentation.model.cards;

import com.google.myjson.annotations.Expose;
import org.hibernate.validator.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created on Sep 30, 2019
 *
 * @author Alexander Petukhov
 */
public class CardApprovalRequestFormObject extends CommonCardApprovalRequestFormObject {

    @Expose
    @NotEmpty(message = "validation.emptyField")
    private String countryCode;

    private MultipartFile frontImage;

    private MultipartFile backImage;

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
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
