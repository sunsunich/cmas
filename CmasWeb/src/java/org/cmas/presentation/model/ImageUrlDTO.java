package org.cmas.presentation.model;

import com.google.myjson.annotations.Expose;

/**
 * Created on Apr 06, 2016
 *
 * @author Alexander Petukhov
 */
public class ImageUrlDTO {

    @Expose
    private boolean success;

    @Expose
    private String imageUrl;

    public ImageUrlDTO(boolean success, String imageUrl) {
        this.success = success;
        this.imageUrl = imageUrl;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
