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
    private String cmasHqEmail;

    private InternetAddress adminMailAddress;
    private InternetAddress supportAddress;
    private InternetAddress questionFormAddress;
    private InternetAddress cmasHqAddress;

    private String hostName;

    private String attachmentsLocalRoot;

    @Override
    public void afterPropertiesSet() throws Exception {
        //subjects = new Subjects(localeConfiguration.getSiteName(null), msgSource);  //// !!!
        supportAddress = new InternetAddress(supportEmail);
        questionFormAddress = new InternetAddress(questionFormEmail);
        adminMailAddress = new InternetAddress(adminMail);
        cmasHqAddress = new InternetAddress(cmasHqEmail);
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

    public String getCmasHqEmail() {
        return cmasHqEmail;
    }

    public InternetAddress getCmasHqAddress() {
        return cmasHqAddress;
    }

    @Required
    public void setCmasHqEmail(String cmasHqEmail) {
        this.cmasHqEmail = cmasHqEmail;
    }

    public String getAttachmentsLocalRoot() {
        return attachmentsLocalRoot;
    }

    @Required
    public void setAttachmentsLocalRoot(String attachmentsLocalRoot) {
        this.attachmentsLocalRoot = attachmentsLocalRoot;
    }
}
