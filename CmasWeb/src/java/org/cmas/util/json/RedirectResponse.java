package org.cmas.util.json;

import com.google.myjson.annotations.Expose;

public class RedirectResponse {

    @Expose
    private Boolean success;

    @Expose
    private String redirectUrl;

    public RedirectResponse() {
    }

    public RedirectResponse(Boolean success, String redirectUrl) {
        this.success = success;
        this.redirectUrl = redirectUrl;
    }

    public Boolean isSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}
