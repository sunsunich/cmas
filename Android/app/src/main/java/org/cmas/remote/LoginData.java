package org.cmas.remote;

public class LoginData {

    public LoginData(String username, String password, String deviceId, String gcmRegId) {
        this.username = username;
        this.password = password;
        this.deviceId = deviceId;
        this.gcmRegId = gcmRegId;
    }

    public String username;
    public String password;
    public String deviceId;
    public String gcmRegId;
}
