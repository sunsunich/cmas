package org.cmas.presentation.model;

import com.google.myjson.annotations.Expose;

/**
 * Created on Apr 06, 2016
 *
 * @author Alexander Petukhov
 */
public class ImageDTO {

    @Expose
    private boolean success;

    @Expose
    private String base64;

    public ImageDTO(boolean success, String base64) {
        this.success = success;
        this.base64 = base64;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }
}
