package org.cmas.util.mail;


import javax.mail.internet.InternetAddress;

/**

 */
public interface MailTransport {
    void sendMail(InternetAddress from, InternetAddress to, String text, String subject, boolean html, String encoding);

    void sendNewsletter(InternetAddress from, InternetAddress[] to, String text, String subject, String encoding);
}
