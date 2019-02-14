package org.cmas.util.mail;

import org.cmas.i18n.MsgKey;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.MessageSource;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

public abstract class CommonMailServiceImpl {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * логика реальной отправки мейлов уехала туда.
     */
    protected MailTransport mailTransport;
    /**
     * конвертирует объекты в тексты сообщений.
     */
    protected TemplateRenderer<ModelAttr> textRenderer;
    /**
     * конвертирует объекты в заголовки сообщений.
     */
    protected TemplateRenderer<Object> subjects;
    /**
     * содержит все настроечные почтовые адреса
     */
    protected MailerConfig addresses;

    // Набор заголовков писем
    protected static final Object[] EMPTY_ARGS = {};
    protected MessageSource msgSource;

    // обернул InternetAddress
    @Nullable
    protected InternetAddress getInternetAddress(String addr) {
        try {
            return new InternetAddress(addr);
        } catch (AddressException e) {
            log.warn("cant convert sting " + addr + " to address", e);
        }
        return null;
    }

    // локализованный вариант sitename
    @Nullable
    protected InternetAddress getSiteReplyAddress(Locale locale) {
        try {
            return new InternetAddress(addresses.getSiteAddress(), addresses.getFromText());
        } catch (UnsupportedEncodingException e) {
            log.error("cant create site reply address for locale " + locale +
                      " site address=" + addresses.getSiteAddress(), e);
        }
        return null;
    }

    protected String getMailEncoding(Locale locale) {
        return msgSource.getMessage(MsgKey.MAIL_ENC, null, locale);
    }

    @Required
    public void setAddresses(MailerConfig addresses) {
        this.addresses = addresses;
    }

    @Required
    public void setMailTransport(MailTransport mailTransport) {
        this.mailTransport = mailTransport;
    }

    public void setTextRenderer(TemplateRenderer textRenderer) {
        this.textRenderer = textRenderer;
    }

    public void setSubjRenderer(TemplateRenderer subjRenderer) {
        subjects = subjRenderer;
    }

    @Required
    public void setMsgSource(MessageSource msgSource) {
        this.msgSource = msgSource;
    }
}
