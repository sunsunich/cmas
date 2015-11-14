package org.cmas.util.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;

public class MailTransportImpl implements MailTransport {
    // Logger instance for class
    private static final Logger log = LoggerFactory.getLogger(MailTransportImpl.class);

    private JavaMailSender mailSender;
    private Session session;
    private TaskExecutor executor;

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public void setExecutor(TaskExecutor executor) {
        this.executor = executor;
    }

    @Override
    public void sendNewsletter(InternetAddress from, InternetAddress[] to, String text, String subject, String encoding) {
        executor.execute(new SendNewsletterTask(to, from, text, subject, true, encoding));
    }


    @Override
    public void sendMail(InternetAddress from, InternetAddress to, String text, String subject, boolean html, String encoding) {
        executor.execute(new SendMailTask(from, to, text, subject, html, encoding));
    }

    private class SendMailTask implements Runnable {
        private final InternetAddress from;
        private final InternetAddress to;
        private final String text;
        private final String subject;
        private final boolean html;
        private final String encoding;

        SendMailTask(InternetAddress from, InternetAddress to, String text, String subject, boolean html,  String encoding) {
            this.from = from;
            this.to = to;
            this.text = text;
            this.subject = subject;
            this.html = html;
       //     this.attachments = attachments;
            this.encoding = encoding;
        }

        @Override
        public void run() {
            MimeMessagePreparator prep = new MimeMessagePreparator() {
                @Override
                public void prepare(MimeMessage mimeMessage) throws MessagingException {
                    boolean isMultiPart = false;//attachments != null;
                    MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, isMultiPart, encoding);
                    messageHelper.setFrom(from);
                    messageHelper.setTo(to);
                    messageHelper.setSubject(subject);
                    // если есть аттачи - добавляем их в письмо.
//                    if (attachments != null) {
//                        messageHelper.setText(text, html);
//                        for (Attachment attachment : attachments) {
//                            messageHelper.addAttachment(attachment.getFileName(), attachment.getDataSource());
//                        }
//                    } else {
                        // а если нет - отдаем текст по старинке.
                        if (html) {
                            messageHelper.getMimeMessage().setContent(text, "text/html; charset=" + messageHelper.getEncoding());
                        } else {
                            messageHelper.setText(text);
                        }
                        messageHelper.getMimeMessage().removeHeader("Content-Transfer-Encoding");
                        messageHelper.getMimeMessage().addHeader("Content-Transfer-Encoding", "base64");
            //        }

                    messageHelper.getMimeMessage().saveChanges();
                }
            };
            try {
                mailSender.send(prep);
            } catch (Exception e) {
                log.error("error send email, \nto=" + to + "\ntext=" + text+" \n subj="+subject, e);
            }
        }
    }

    private class SendNewsletterTask implements Runnable {
        private final InternetAddress from;
        private final InternetAddress[] to;
        private final String text;
        private final String subject;
        private final boolean html;
        private final String encoding;

        private SendNewsletterTask(InternetAddress[] to, InternetAddress from, String text, String subject, boolean html, String encoding) {
            this.to = to;
            this.from = from;
            this.subject = subject;
            this.text = text;
            this.html = html;
            this.encoding = encoding;
        }

        @Override
        public void run() {
            try {
                Transport transport = session.getTransport();
                transport.connect();
                MimeMessage message = new MimeMessage(session);
                message.setFrom(from);
                message.setRecipients(Message.RecipientType.TO, new Address[]{from});
                message.setSubject(subject);

                if (html) {
                    message.setContent(text, "text/html; charset=" + encoding);
                } else {
                    message.setText(text, encoding);
                }
                message.removeHeader("Content-Transfer-Encoding");
                message.addHeader("Content-Transfer-Encoding", "base64");
                message.saveChanges();
                transport.sendMessage(message, to);
            } catch (Exception e) {
                log.error("error send newsletter, \ntext=" + text + "\n subj = " + subject + "\n to=" + Arrays.toString(to), e);
            }
        }
    }

}
