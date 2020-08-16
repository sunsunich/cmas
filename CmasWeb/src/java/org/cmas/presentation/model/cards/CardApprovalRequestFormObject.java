package org.cmas.presentation.model.cards;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created on Sep 30, 2019
 *
 * @author Alexander Petukhov
 */
public class CardApprovalRequestFormObject extends CommonCardApprovalRequestFormObject {

    private MultipartFile frontImage;

    private MultipartFile backImage;

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
