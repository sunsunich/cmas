package org.cmas.util.mail;


import javax.mail.internet.InternetAddress;
import java.util.List;

/**

 */
public interface MailTransport {
    void sendMail(InternetAddress from, InternetAddress to, String text, String subject, boolean html, String encoding);

    void sendNewsletter(InternetAddress from, InternetAddress[] to, String text, String subject, String encoding);

    void sendMail(InternetAddress from,
                  InternetAddress to,
                  String text,
                  String subject,
                  boolean html,
                  List<Attachment> attachments,
                  String encoding);
}
