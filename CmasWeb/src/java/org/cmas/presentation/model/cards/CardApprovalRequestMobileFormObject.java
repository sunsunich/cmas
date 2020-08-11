package org.cmas.presentation.model.cards;

import com.google.myjson.annotations.Expose;

import java.util.List;

/**
 * Created on Sep 30, 2019
 *
 * @author Alexander Petukhov
 */
public class CardApprovalRequestMobileFormObject extends CommonCardApprovalRequestFormObject {

    @Expose
    private List<String> images;

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
