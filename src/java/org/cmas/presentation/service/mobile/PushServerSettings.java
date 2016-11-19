package org.cmas.presentation.service.mobile;

import org.springframework.beans.factory.annotation.Required;

public class PushServerSettings {//} implements InitializingBean{

    private String gcmKey;
    private String iosKeystorePath;
    private String iosKeystorePssword;

//    @Override
//    public void afterPropertiesSet() throws Exception {
//        FirebaseOptions options = new FirebaseOptions.Builder()
//                .setServiceAccount(getClass().getResourceAsStream("google-services.json"))
//                .build();
//        FirebaseApp.initializeApp(options);
//    }

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
