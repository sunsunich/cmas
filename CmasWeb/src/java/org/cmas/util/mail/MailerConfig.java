package org.cmas.util.mail;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

import javax.mail.internet.InternetAddress;
import java.util.Locale;

/**
 */
public class MailerConfig implements InitializingBean, AddressConfig {
    // Почтовый адрес системы для siteReplyAddress
    private String siteAddress;
    private String fromText;

    // Почтовый адрес supprt'а для supportAddress
    private String supportEmail;
    // Администратор системы
    private String adminMail;
    private String questionFormEmail;

    private InternetAddress adminMailAddress;
    private InternetAddress supportAddress;
    private InternetAddress questionFormAddress;

    private String hostName;


    @Override
    public void afterPropertiesSet() throws Exception {
        //subjects = new Subjects(localeConfiguration.getSiteName(null), msgSource);  //// !!!
        supportAddress = new InternetAddress(supportEmail);
        questionFormAddress = new InternetAddress(questionFormEmail);
        adminMailAddress = new InternetAddress(adminMail);
    }

    public void setSiteAddress(String siteAddress) {
        this.siteAddress = siteAddress;
    }

    @Required
    public void setSupportEmail(String supportEmail) {
        this.supportEmail = supportEmail;
    }

    @Required
    public void setQuestionFormEmail(String questionFormEmail) {
        this.questionFormEmail = questionFormEmail;
    }

    public void setAdminMail(String adminMail) {
        this.adminMail = adminMail;
    }

    @Override
    public String getQuestionFormAddr() {
        return questionFormEmail;
    }

    public String getAdminMail() {
        return adminMail;
    }

    public void setSupportAddress(InternetAddress supportAddress) {
        this.supportAddress = supportAddress;
    }

    public void setQuestionFormAddress(InternetAddress questionFormAddress) {
        this.questionFormAddress = questionFormAddress;
    }

    public InternetAddress getAdminMailAddress() {
        return adminMailAddress;
    }

    public void setAdminMailAddress(InternetAddress adminMailAddress) {
        this.adminMailAddress = adminMailAddress;
    }

    @Override
    public String getSiteAddress() {
        return siteAddress;
    }

    public String getSupportEmail() {
        return supportEmail;
    }

    public String getQuestionFormEmail() {
        return questionFormEmail;
    }

    public InternetAddress getSupportAddress() {
        return supportAddress;
    }

    public InternetAddress getQuestionFormAddress() {
        return questionFormAddress;
    }


    @Override
    public String getSiteName(Locale locale) {
        return hostName;
    }

    @Override
    public String getSiteWebAddress(Locale locale) {
        return "http://" + hostName;
    }

    @Override
    public String getSupportAddr() {
        return supportEmail;
    }

    @Required
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getFromText() {
        return fromText;
    }

    @Required
    public void setFromText(String fromText) {
        this.fromText = fromText;
    }
}
