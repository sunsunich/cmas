package org.cmas.presentation.service.mail;

import org.cmas.Globals;
import org.cmas.presentation.entities.InternetAddressOwner;
import org.cmas.presentation.entities.billing.Invoice;
import org.cmas.presentation.entities.user.Registration;
import org.cmas.presentation.entities.user.UserClient;
import org.cmas.util.mail.CommonMailServiceImpl;
import org.cmas.util.mail.ModelAttr;
import org.jetbrains.annotations.Nullable;

import javax.mail.internet.InternetAddress;
import java.util.Locale;

/**
 * отвечает за формирование текстов сообщений из темплейтов и информации.
 */
public class MailServiceImpl extends CommonMailServiceImpl implements MailService {

    private String getTemplateName(Registration reg) {
        return "sendConfirmation.ftl";
    }

    /**
     * Отправляет пользователю подтверждение регистрации
     */
    @Override
    public void confirmRegistrator(Registration reg) {
        Locale locale = reg.getLocale();
        String text = textRenderer.renderText(getTemplateName(reg), locale, new ModelAttr("reg", reg));
        String subj = subjects.renderText("User", locale, addresses.getSiteName(locale));
        InternetAddress from = getSiteReplyAddress(locale);
        InternetAddress to = addresses.getAdminMailAddress();
        mailTransport.sendMail(from, to, text, subj, true, getMailEncoding(locale));
    }

    /**
     * Отправляет пользователю подтверждение о смене email
     */
    @Override
    public void confirmChangeEmail(UserClient user) {
        Locale locale = localeResolver.getDefaultLocale();
        String text = textRenderer.renderText("sendEmailConfirmation.ftl", locale, new ModelAttr("user", user));
        String subj = subjects.renderText("ChangeEmail", locale, addresses.getSiteName(locale));
        InternetAddress from = getSiteReplyAddress(locale);
        InternetAddress to = getInternetAddress(user.getNewMail());
        mailTransport.sendMail(from, to, text, subj, true, getMailEncoding(locale));
    }


    @Override
    public void sendLostPasswd(UserClient user) {
        Locale locale = localeResolver.getDefaultLocale();
        String subj = subjects.renderText("LostPasswd", locale, addresses.getSiteName(locale));
        String text = textRenderer.renderText("lostPasswd.ftl", locale, new ModelAttr("user", user));
        InternetAddress from = getSiteReplyAddress(locale);
        InternetAddress to = getInternetAddress(user);
        mailTransport.sendMail(from, to, text, subj, true, getMailEncoding(locale));
    }

    /**
     * Отправляем пользователю сообщение, об успешной активации его в системе
     */
    @Override
    public void regCompleteNotify(UserClient user) {
        Locale locale = localeResolver.getDefaultLocale();

        String text = textRenderer.renderText(
                "regComplete.ftl", locale,
                new ModelAttr("user", user)
        );
        InternetAddress to = getInternetAddress(user);
        InternetAddress from = getSiteReplyAddress(locale);
        String subj = subjects.renderText("RegistrationComplete", locale, addresses.getSiteName(locale));
        mailTransport.sendMail(from, to, text, subj, true, getMailEncoding(locale));
    }

    @Override
    public void confirmPayment(Invoice invoice) {
        Locale locale = localeResolver.getDefaultLocale();
        String invoiceTypeStr = getInvoiceTypeStrForMail(invoice);

        String text = textRenderer.renderText(
                "paymentConfirm.ftl", locale,
                  new ModelAttr("invoice", invoice)
                , new ModelAttr("invoiceType", invoiceTypeStr)
                , new ModelAttr("date", Globals.getDTF().format(invoice.getCreateDate().getTime()))
        );

        UserClient user = invoice.getUser();
        InternetAddress to = getInternetAddress(user);
        InternetAddress from = getSiteReplyAddress(locale);
        //String from = addresses.getFromText();
        String subj = subjects.renderText("MoneyIncome", locale, addresses.getSiteName(locale));
        mailTransport.sendMail(from, to, text, subj, true, getMailEncoding(locale));
    }

    @Override
    public void paymentFailed(Invoice invoice) {
        Locale locale = localeResolver.getDefaultLocale();
        String invoiceTypeStr = getInvoiceTypeStrForMail(invoice);

        String text = textRenderer.renderText(
                "paymentFailed.ftl", locale,
                  new ModelAttr("invoice", invoice)
                , new ModelAttr("invoiceType", invoiceTypeStr)
                , new ModelAttr("date", Globals.getDTF().format(invoice.getCreateDate().getTime()))
        );

        InternetAddress from = getSiteReplyAddress(locale);
        InternetAddress to = addresses.getAdminMailAddress();
        //String from = addresses.getFromText();
        String subj = subjects.renderText("MoneyIncomeFailed", locale, addresses.getSiteName(locale));
        mailTransport.sendMail(from, to, text, subj, true, getMailEncoding(locale));
    }

    private String getInvoiceTypeStrForMail(Invoice invoice) {
        String invoiceTypeStr = "";
        switch (invoice.getInvoiceType()) {
            case INTERKASSA:
                invoiceTypeStr = "Interkassa";
                break;
        }
        return invoiceTypeStr;
    }

    // обернул UnsupportedEncodingException
    @Nullable
    private InternetAddress getInternetAddress(InternetAddressOwner user) {
        InternetAddress internetAddress = null;
        try {
            internetAddress = user.getInternetAddress();
        } catch (Exception e) {
            log.error("Can't get mail from user " + user.toString(), e);
        }
        return internetAddress;
    }

}

