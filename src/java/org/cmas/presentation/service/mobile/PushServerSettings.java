package org.cmas.presentation.service.mobile;

import org.springframework.beans.factory.annotation.Required;

public class PushServerSettings {

    private String gcmKey;
    private String iosKeystorePath;
    private String iosKeystorePssword;

    public String getGcmKey() {
        return gcmKey;
    }

    @Required
    public void setGcmKey(String gcmKey) {
        this.gcmKey = gcmKey;
    }

    public String getIosKeystorePath() {
        return iosKeystorePath;
    }

    @Required
    public void setIosKeystorePath(String iosKeystorePath) {
        this.iosKeystorePath = iosKeystorePath;
    }

    public String getIosKeystorePssword() {
        return iosKeystorePssword;
    }

    @Required
    public void setIosKeystorePssword(String iosKeystorePssword) {
        this.iosKeystorePssword = iosKeystorePssword;
    }
}
