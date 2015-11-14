package org.cmas.util.presentation.controller;

import com.google.myjson.annotations.Expose;

public class LoginRedirectGsonResponse {

    @Expose
    private Boolean success;

    @Expose
    private String redirectUrl;

    public LoginRedirectGsonResponse() {
    }

    public LoginRedirectGsonResponse(Boolean success, String redirectUrl) {
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
