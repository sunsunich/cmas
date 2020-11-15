package org.cmas.app;

public class Settings {

    private String deviceId;
    private String gcmRegistrationId;
    private String currentUsername;
    private String currentPassword;
    private String jsessionid;

    public Settings(String deviceId,
                    String gcmRegistrationId,
                    String currentUsername,
                    String currentPassword,
                    String jsessionid
    ) {
        this.deviceId = deviceId;
        this.currentUsername = currentUsername;
        this.currentPassword = currentPassword;
        this.gcmRegistrationId = gcmRegistrationId;
        this.jsessionid = jsessionid;
    }
    public void clear(){
        currentUsername = "";
        currentPassword = "";
        jsessionid = "";
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getCurrentUsername() {
        return currentUsername;
    }

    public void setCurrentUsername(String currentUsername) {
        this.currentUsername = currentUsername;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getGcmRegistrationId() {
        return gcmRegistrationId;
    }

    public void setGcmRegistrationId(String gcmRegistrationId) {
        this.gcmRegistrationId = gcmRegistrationId;
    }

    public String getJsessionid() {
        return jsessionid;
    }

    public void setJsessionid(String jsessionid) {
        this.jsessionid = jsessionid;
    }

}
