package org.cmas.presentation.model.admin;


public class PasswordChangeFormObject {
    private Long userId;

    private String passwd;
    private String passwdRe;

    public PasswordChangeFormObject() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getPasswdRe() {
        return passwdRe;
    }

    public void setPasswdRe(String passwdRe) {
        this.passwdRe = passwdRe;
    }
}
