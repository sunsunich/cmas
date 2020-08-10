package org.cmas.presentation.service.mail;

import org.cmas.Globals;
import org.cmas.backend.ImageStorageManager;
import org.cmas.entities.FeedbackItem;
import org.cmas.entities.User;
import org.cmas.entities.UserFile;
import org.cmas.entities.billing.Invoice;
import org.cmas.entities.cards.CardApprovalRequest;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.diver.DiverRegistrationStatus;
import org.cmas.entities.logbook.DiverFriendRequest;
import org.cmas.entities.logbook.LogbookBuddieRequest;
import org.cmas.entities.logbook.LogbookEntry;
import org.cmas.entities.loyalty.CameraOrder;
import org.cmas.entities.loyalty.InsuranceRequest;
import org.cmas.entities.loyalty.PaidFeature;
import org.cmas.presentation.entities.InternetAddressOwner;
import org.cmas.presentation.entities.user.BackendUser;
import org.cmas.presentation.entities.user.Registration;
import org.cmas.util.mail.CommonMailServiceImpl;
import org.cmas.util.mail.ModelAttr;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;

import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Locale;

public class MailServiceImpl extends CommonMailServiceImpl implements MailService {

    @Override
    public void sendDiverPassword(Diver diver) {
        Locale locale = diver.getLocale();
        String subj = subjects.renderText("SetupPasswd", locale, addresses.getSiteName(locale));
        String text = textRenderer.renderText("setupPasswd.ftl", locale,
                                              new ModelAttr("diver", diver)
        );
        InternetAddress from = getSiteReplyAddress(locale);
        InternetAddress to = getInternetAddress(diver);
        mailTransport.sendMail(from, to, text, subj, true, getMailEncoding(locale));
    }

    @Override
    public void sendRegistration(Registration reg) {
        Locale locale = reg.getLocale();
        String text = textRenderer.renderText("sendRegistration.ftl", locale
                , new ModelAttr("reg", reg)
        );
        String subj = subjects.renderText("Registration", locale, addresses.getSiteName(locale));
        InternetAddress from = getSiteReplyAddress(locale);
        InternetAddress to = getInternetAddress(reg);
        mailTransport.sendMail(from, to, text, subj, true, getMailEncoding(locale));
    }

    @Override
    public void regCompleteNotify(User user) {
        Locale locale = user.getLocale();

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
    public void confirmChangeEmail(User user) {
        Locale locale = user.getLocale();
        String text = textRenderer.renderText("sendEmailConfirmation.ftl", locale, new ModelAttr("user", user));
        String subj = subjects.renderText("ChangeEmail", locale, addresses.getSiteName(locale));
        InternetAddress from = getSiteReplyAddress(locale);
        InternetAddress to = getInternetAddress(user.getNewMail());
        mailTransport.sendMail(from, to, text, subj, true, getMailEncoding(locale));
    }

    @Override
    public void sendLostPasswd(User user) {
        Locale locale = user.getLocale();
        String subj = subjects.renderText("LostPasswd", locale, addresses.getSiteName(locale));
        String text = textRenderer.renderText("lostPasswd.ftl", locale, new ModelAttr("user", user));
        InternetAddress from = getSiteReplyAddress(locale);
        InternetAddress to = getInternetAddress(user);
        mailTransport.sendMail(from, to, text, subj, true, getMailEncoding(locale));
    }

    @Override
    public void sendFriendRequest(DiverFriendRequest friendRequest) {
        Diver diver = friendRequest.getTo();
        Locale locale = diver.getLocale();
        String siteName = addresses.getSiteName(locale);
        String subj = subjects.renderText("FriendRequest", locale, siteName);
        String text = textRenderer.renderText("friendRequest.ftl", locale,
                                              new ModelAttr("request", friendRequest),
                                              new ModelAttr("siteName", siteName)
        );
        InternetAddress from = getSiteReplyAddress(locale);
        InternetAddress to = getInternetAddress(diver);
        mailTransport.sendMail(from, to, text, subj, true, getMailEncoding(locale));
    }

    @Override
    public void sendToInstructorToApprove(LogbookBuddieRequest instructorRequest) {
        Diver diver = instructorRequest.getTo();
        Locale locale = diver.getLocale();
        String siteName = addresses.getSiteName(locale);
        String subj = subjects.renderText("InstructorVerificationRequest", locale, siteName);
        String text = textRenderer.renderText("instructorApproveRequest.ftl", locale,
                                              new ModelAttr("request", instructorRequest),
                                              new ModelAttr("diveDate",
                                                            Globals.getDTF()
                                                                   .format(instructorRequest.getLogbookEntry()
                                                                                            .getDiveDate())),
                                              new ModelAttr("siteName", siteName)
        );
        InternetAddress from = getSiteReplyAddress(locale);
        InternetAddress to = getInternetAddress(diver);
        mailTransport.sendMail(from, to, text, subj, true, getMailEncoding(locale));
    }

    @Override
    public void sendInstructorApproved(LogbookBuddieRequest instructorRequest) {
        Diver diver = instructorRequest.getTo();
        Locale locale = diver.getLocale();
        String siteName = addresses.getSiteName(locale);
        String subj = subjects.renderText("InstructorApproved", locale, siteName);
        String text = textRenderer.renderText("instructorVerifiedDiver.ftl", locale,
                                              new ModelAttr("request", instructorRequest),
                                              new ModelAttr("diveDate",
                                                            Globals.getDTF()
                                                                   .format(instructorRequest.getLogbookEntry()
                                                                                            .getDiveDate())),
                                              new ModelAttr("siteName", siteName)
        );
        InternetAddress from = getSiteReplyAddress(locale);
        InternetAddress to = getInternetAddress(instructorRequest.getFrom());
        mailTransport.sendMail(from, to, text, subj, true, getMailEncoding(locale));
    }

    @Override
    public void confirmPayment(Invoice invoice) {
        Locale locale = invoice.getUser().getLocale();
        String invoiceTypeStr = getInvoiceTypeStrForMail(invoice);
        boolean hasCmasLicenceFeature = false;
        boolean hasGoldFeature = false;
        for (PaidFeature paidFeature : invoice.getRequestedPaidFeatures()) {
            if (paidFeature.getId() == Globals.CMAS_LICENCE_PAID_FEATURE_DB_ID) {
                hasCmasLicenceFeature = true;
            } else if (paidFeature.getId() == Globals.GOLD_MEMBERSHIP_PAID_FEATURE_DB_ID) {
                hasGoldFeature = true;
            }
        }

        String text = textRenderer.renderText(
                "paymentConfirm.ftl", locale,
                new ModelAttr("invoice", invoice)
                , new ModelAttr("invoiceType", invoiceTypeStr)
                , new ModelAttr("hasCmasLicenceFeature", hasCmasLicenceFeature)
                , new ModelAttr("hasGoldFeature", hasGoldFeature)
                , new ModelAttr("date", Globals.getDTF().format(invoice.getCreateDate().getTime()))
        );

        User user = invoice.getDiver();
        InternetAddress to = getInternetAddress(user);
        InternetAddress from = getSiteReplyAddress(locale);
        //String from = addresses.getFromText();
        String subj = subjects.renderText("MoneyIncome", locale, addresses.getSiteName(locale));
        mailTransport.sendMail(from, to, text, subj, true, getMailEncoding(locale));
    }

    @Override
    public void paymentFailed(Invoice invoice) {
        Locale locale = invoice.getUser().getLocale();
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

    private static String getInvoiceTypeStrForMail(Invoice invoice) {
        String invoiceTypeStr = "";
        switch (invoice.getInvoiceType()) {
            case SYSTEMPAY:
                invoiceTypeStr = "Systempay";
                break;
        }
        return invoiceTypeStr;
    }

    @Nullable
    private InternetAddress getInternetAddress(User user) {
        return getInternetAddress(new BackendUser(user));
    }

    @Nullable
    private InternetAddress getInternetAddress(InternetAddressOwner user) {
        InternetAddress internetAddress = null;
        try {
            internetAddress = user.getInternetAddress();
        } catch (Exception e) {
            log.error("Can't get mail from user {}", user, e);
        }
        return internetAddress;
    }

    @Override
    public void sendCameraOrderMailToDiver(CameraOrder cameraOrder) {
        Diver diver = cameraOrder.getDiver();
        Locale locale = diver.getLocale();
        String cameraName = cameraOrder.getCameraName();
        String subj = subjects.renderText("CameraOrderConfirmed", locale, cameraName);
        String text = textRenderer.renderText("cameraOrderConfirmed.ftl", locale,
                                              new ModelAttr("diver", diver),
                                              new ModelAttr("sendToEmail", cameraOrder.getSendToEmail()),
                                              new ModelAttr("cameraName", cameraName),
                                              new ModelAttr("referenceNumber", cameraOrder.getExternalNumber())
        );
        InternetAddress from = getSiteReplyAddress(locale);
        InternetAddress to = getInternetAddress(diver);
        mailTransport.sendMail(from, to, text, subj, true, getMailEncoding(locale));
    }

    @Override
    public void sendCameraOrderMailToSubal(CameraOrder cameraOrder) {
        Diver diver = cameraOrder.getDiver();
        Locale locale = Locale.ENGLISH;
        String cameraName = cameraOrder.getCameraName();
        String subj = subjects.renderText("CameraOrderRequest",
                                          locale,
                                          cameraName,
                                          diver.getPrimaryPersonalCard().getPrintNumber());
        String text;
        if (diver.getDiverRegistrationStatus() == DiverRegistrationStatus.GUEST) {
            text = textRenderer.renderText("cameraOrderRequestGuest.ftl", locale,
                                           new ModelAttr("diver", diver),
                                           new ModelAttr("cameraName", cameraName),
                                           new ModelAttr("referenceNumber", cameraOrder.getExternalNumber())
            );
        } else {
            text = textRenderer.renderText("cameraOrderRequest.ftl", locale,
                                           new ModelAttr("diver", diver),
                                           new ModelAttr("cameraName", cameraName),
                                           new ModelAttr("country", diver.getFederation().getCountry()),
                                           new ModelAttr("referenceNumber", cameraOrder.getExternalNumber())
            );
        }
        InternetAddress from = getSiteReplyAddress(locale);
        try {
            InternetAddress to = new InternetAddress(cameraOrder.getSendToEmail(), "SUBAL representative");
            mailTransport.sendMail(from, to, text, subj, true, getMailEncoding(locale));
        } catch (UnsupportedEncodingException e) {
            log.error("cant create site reply address for locale " + locale +
                      " site address=" + addresses.getSiteAddress(), e);
        }
    }

    @Override
    public void noInsuranceRequestForInvoice(Invoice invoice) {
        Locale locale = Locale.ENGLISH;
        String text = textRenderer.renderText(
                "noInsuranceRequestForInvoice.ftl", locale,
                new ModelAttr("invoice", invoice)
        );
        InternetAddress from = getSiteReplyAddress(locale);
        InternetAddress to = addresses.getAdminMailAddress();
        String subj = subjects.renderText("NoInsuranceRequestForInvoice", locale, invoice.getExternalInvoiceNumber());
        mailTransport.sendMail(from, to, text, subj, true, getMailEncoding(locale));
    }

    @Override
    public void sendInsuranceRequestFailed(InsuranceRequest insuranceRequest, String message) {
        Locale locale = Locale.ENGLISH;
        String text = textRenderer.renderText(
                "insuranceRequestFailed.ftl", locale,
                new ModelAttr("insuranceRequest", insuranceRequest),
                new ModelAttr("reason", message)
        );
        InternetAddress from = getSiteReplyAddress(locale);
        InternetAddress to = addresses.getAdminMailAddress();
        String subj = subjects.renderText("InsuranceRequestFailed", locale, insuranceRequest.getId());
        mailTransport.sendMail(from, to, text, subj, true, getMailEncoding(locale));
    }

    @Autowired
    private ImageStorageManager imageStorageManager;

    @Override
    public void sendLogbookEntryChanged(LogbookEntry logbookEntry) {
        Locale locale = Locale.ENGLISH;
        String text = textRenderer.renderText(
                "logbookEntryChanged.ftl", locale,
                new ModelAttr("id", logbookEntry.getId()),
                new ModelAttr("photoUrl", logbookEntry.getPhotoUrl() == null ? "" :
                        addresses.getSiteName(locale) +
                        imageStorageManager.getLogbookEntryImagesRoot() + logbookEntry.getPhotoUrl()),
                new ModelAttr("name", logbookEntry.getName() == null ? "" : logbookEntry.getName()),
                new ModelAttr("note", logbookEntry.getNote() == null ? "" : logbookEntry.getNote()),
                new ModelAttr("decoStepsComments",
                              logbookEntry.getDiveSpec().getDecoStepsComments() == null ?
                                      "" :
                                      logbookEntry.getDiveSpec().getDecoStepsComments()),
                new ModelAttr("cnsToxicity",
                              logbookEntry.getDiveSpec().getCnsToxicity() == null ?
                                      "" :
                                      logbookEntry.getDiveSpec().getCnsToxicity())
        );
        InternetAddress from = getSiteReplyAddress(locale);
        InternetAddress to = addresses.getAdminMailAddress();
        String subj = subjects.renderText("LogbookEntryChanged", locale, logbookEntry.getId());
        mailTransport.sendMail(from, to, text, subj, true, getMailEncoding(locale));
    }

    @Override
    public void sendFeedbackItem(FeedbackItem feedbackItem) {
        Locale locale = Locale.ENGLISH;
        String imageUrl1 = "";
        String imageUrl2 = "";
        List<UserFile> files = feedbackItem.getFiles();
        if (files != null && !files.isEmpty()) {
            imageUrl1 = addresses.getSiteName(locale) +
                        imageStorageManager.getFeedbackImagesRoot() + files.get(0).getFileUrl();
            if (files.size() > 1) {
                imageUrl2 = addresses.getSiteName(locale) +
                            imageStorageManager.getFeedbackImagesRoot() + files.get(1).getFileUrl();
            }
        }
        String text = textRenderer.renderText(
                "feedbackSubmitted.ftl", locale,
                new ModelAttr("id", feedbackItem.getId()),
                new ModelAttr("diver", feedbackItem.getCreator()),
                new ModelAttr("text", feedbackItem.getText()),
                new ModelAttr("imageUrl1", imageUrl1),
                new ModelAttr("imageUrl2", imageUrl2),
                new ModelAttr("spotId", feedbackItem.getDiveSpot() == null ? "" : feedbackItem.getDiveSpot().getId()),
                new ModelAttr("logbookEntryId",
                              feedbackItem.getLogbookEntry() == null ? "" : feedbackItem.getLogbookEntry().getId())
        );
        InternetAddress from = getSiteReplyAddress(locale);
        InternetAddress to = addresses.getAdminMailAddress();
        String subj = subjects.renderText("FeedbackSubmitted", locale, feedbackItem.getId());
        mailTransport.sendMail(from, to, text, subj, true, getMailEncoding(locale));
    }

    @Override
    public void sendFeedbackItemToUser(FeedbackItem feedbackItem) {
        Locale locale = Locale.ENGLISH;
        String imageUrl1 = null;
        String imageUrl2 = null;
        List<UserFile> files = feedbackItem.getFiles();
        if (files != null && !files.isEmpty()) {
            imageUrl1 = addresses.getSiteName(locale) +
                        imageStorageManager.getFeedbackImagesRoot() + files.get(0).getFileUrl();
            if (files.size() > 1) {
                imageUrl2 = addresses.getSiteName(locale) +
                            imageStorageManager.getFeedbackImagesRoot() + files.get(1).getFileUrl();
            }
        }
        Diver creator = feedbackItem.getCreator();
        String text = textRenderer.renderText(
                "feedbackSubmittedUser.ftl", locale,
                new ModelAttr("id", feedbackItem.getId()),
                new ModelAttr("diver", creator),
                new ModelAttr("text", feedbackItem.getText()),
                new ModelAttr("imageUrl1", imageUrl1),
                new ModelAttr("imageUrl2", imageUrl2),
                new ModelAttr("spotName",
                              feedbackItem.getDiveSpot() == null ? null : feedbackItem.getDiveSpot().getLatinName()),
                new ModelAttr("logbookEntryId",
                              feedbackItem.getLogbookEntry() == null ? "" : feedbackItem.getLogbookEntry().getId())
        );
        InternetAddress from = getSiteReplyAddress(locale);
        InternetAddress to = getInternetAddress(creator);
        String subj = subjects.renderText("FeedbackSubmitted", locale, feedbackItem.getId());
        mailTransport.sendMail(from, to, text, subj, true, getMailEncoding(locale));
    }

    @Override
    public void sendCardApprovalRequestToAquaLinkAdmin(CardApprovalRequest cardApprovalRequest) {
        Locale locale = Locale.ENGLISH;
        String frontImage = addresses.getSiteName(locale) +
                            imageStorageManager.getCardApprovalRequestImagesRoot() +
                            cardApprovalRequest.getFrontImage().getFileUrl();
        String backImage = cardApprovalRequest.getBackImage() == null ? ""
                : addresses.getSiteName(locale) +
                  imageStorageManager.getCardApprovalRequestImagesRoot() +
                  cardApprovalRequest.getBackImage().getFileUrl();
        Diver diver = cardApprovalRequest.getDiver();
        String text = textRenderer.renderText(
                "cardApprovalRequestToAquaLinkAdmin.ftl", locale,
                new ModelAttr("id", cardApprovalRequest.getId()),
                new ModelAttr("diver", diver),
                new ModelAttr("cardApprovalRequest", cardApprovalRequest),
                new ModelAttr("frontImage", frontImage),
                new ModelAttr("backImage", backImage)
        );
        InternetAddress from = getSiteReplyAddress(locale);
        InternetAddress to = addresses.getAdminMailAddress();
        String subj = subjects.renderText("CardApprovalRequest",
                                          locale,
                                          diver.getFirstName() + ' ' + diver.getLastName()
        );
        mailTransport.sendMail(from, to, text, subj, true, getMailEncoding(locale));
    }
}

