package org.cmas.util.mail;

import org.springframework.context.MessageSource;

import java.util.Locale;

/**
 */
public class MessageRenderer implements TemplateRenderer<Object> {

    private MessageSource msgSource;
    private String codePrefix = "mailer.subject.";

    public void setMsgSource(MessageSource msgSource) {
        this.msgSource = msgSource;
    }

    public void setCodePrefix(String codePrefix) {
        this.codePrefix = codePrefix;
    }

    @Override
    public String renderText(String templateName, Locale locale, Object[] modelMembers) {
        return msgSource.getMessage(codePrefix + templateName, modelMembers, locale);
    }
}
