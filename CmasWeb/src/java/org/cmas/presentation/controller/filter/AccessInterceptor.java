package org.cmas.presentation.controller.filter;

import com.google.myjson.Gson;
import org.cmas.entities.User;
import org.cmas.entities.billing.Invoice;
import org.cmas.entities.cards.PersonalCard;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.diver.DiverRegistrationStatus;
import org.cmas.entities.logbook.LogbookEntry;
import org.cmas.presentation.dao.billing.InvoiceDao;
import org.cmas.presentation.dao.cards.PersonalCardDao;
import org.cmas.presentation.dao.logbook.LogbookEntryDao;
import org.cmas.presentation.entities.user.BackendUser;
import org.cmas.presentation.service.AuthenticationService;
import org.cmas.util.json.RedirectResponse;
import org.cmas.util.presentation.spring.WithAjaxAuthenticationProcessingFilterEntryPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AccessInterceptor extends HandlerInterceptorAdapter {

    public static final String INVOICE_ID = "invoiceId";
    public static final String ORDER_ID = "orderId";
    public static final String CARD_ID = "cardId";
    public static final String LOGBOOK_ENTRY_ID = "logbookEntryId";

    private static final String[] PARAMS = {
            INVOICE_ID, ORDER_ID, CARD_ID, LOGBOOK_ENTRY_ID
    };

    @Autowired
    protected AuthenticationService authenticationService;
    @Autowired
    private InvoiceDao invoiceDao;
    @Autowired
    private PersonalCardDao personalCardDao;
    @Autowired
    private LogbookEntryDao logbookEntryDao;

    private final Logger log = LoggerFactory.getLogger(getClass());

    private List<String> exceptions = new ArrayList<>();
    private List<String> freePages = new ArrayList<>();
    private List<String> cmasBasicPages = new ArrayList<>();
    private List<String> cmasBasicFreePages = new ArrayList<>();
    private List<String> demoPages = new ArrayList<>();
    private List<String> guestPages = new ArrayList<>();
    private List<String> goldPages = new ArrayList<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (authenticationService.isAdmin()) {
            return true;
        }
        String requestURI = request.getRequestURI();
        // todo finish for android - probably need a separate interceptor
        /*
            https://www.cmasdata.org/verify
            https://www.foo.com/bar/BlahBlah will redirect to https://play.google.com/store/apps/details?id=com.bar.foo&referrer=BlahBlah
             */
//        if (requestURI.startsWith("/verify?token=") || requestURI.startsWith("/login?token=")) {
//            String referer = "";
//            response.sendRedirect("https://play.google.com/store/apps/details?id=com.cmas.cmas_flutter&referrer=" + referer);
//            return false;
//        }

        if (!requestURI.startsWith("/secure/")) {
            return true;
        }
        if (freePages.contains(requestURI)) {
            return rejectIfCommonValidationNotPassed(request, response, requestURI);
        }
        if (authenticationService.isDiver()) {
            BackendUser<? extends User> currentUser = authenticationService.getCurrentUser();
            Diver diver = (Diver) currentUser.getUser();
            if (goldPages.contains(requestURI)) {
                if (diver.isGold()) {
                    return rejectIfCommonValidationNotPassed(request, response, requestURI);
                } else {
                    redirectForPayment(request, response);
                    return false;
                }
            }
            switch (diver.getDiverRegistrationStatus()) {
                case DEMO:
                    if (diver.getDateLicencePaymentIsDue().after(new Date())
                        && demoPages.contains(requestURI)) {
                        return rejectIfCommonValidationNotPassed(request, response, requestURI);
                    } else {
                        redirectForPayment(request, response);
                        return false;
                    }
                case GUEST:
                    if (demoPages.contains(requestURI) || guestPages.contains(requestURI)) {
                        return rejectIfCommonValidationNotPassed(request, response, requestURI);
                    } else {
                        redirectForPayment(request, response);
                        return false;
                    }
                case CMAS_BASIC:
                    if (diver.getPreviousRegistrationStatus() == DiverRegistrationStatus.NEVER_REGISTERED) {
                        // treat as Demo
                        if (diver.getDateLicencePaymentIsDue().after(new Date()) && demoPages.contains(requestURI)
                            || cmasBasicFreePages.contains(requestURI)
                        ) {
                            return rejectIfCommonValidationNotPassed(request, response, requestURI);
                        } else {
                            redirectForPayment(request, response);
                            return false;
                        }
                    }
                    if (demoPages.contains(requestURI)
                        || cmasBasicFreePages.contains(requestURI)
                        || cmasBasicPages.contains(requestURI)) {
                        return rejectIfCommonValidationNotPassed(request, response, requestURI);
                    } else {
                        redirectForPayment(request, response);
                        return false;
                    }
                case CMAS_FULL:
                    return rejectIfCommonValidationNotPassed(request, response, requestURI);
                case NEVER_REGISTERED:
                    // fall through
                case INACTIVE:
                    redirectForPayment(request, response);
                    return false;
            }
        }
        return rejectIfCommonValidationNotPassed(request, response, requestURI);
    }

    private boolean rejectIfCommonValidationNotPassed(HttpServletRequest request, HttpServletResponse response, String requestURI) throws IOException {
        if (commonValidation(request)) {
            return true;
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            log.warn("cannot access url{}", requestURI);
            return false;
        }
    }

    private static void redirectForPayment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (WithAjaxAuthenticationProcessingFilterEntryPoint.isAjaxRequest(request)) {
            response.setContentType("application/json;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Cache-Control", "no-cache");
            response.setStatus(HttpURLConnection.HTTP_OK);
            new Gson().toJson(new RedirectResponse(false, "/secure/pay.html"), response.getWriter());
        } else {
            response.sendRedirect("/secure/pay.html");
        }
    }

    private boolean commonValidation(HttpServletRequest req) {
        return exceptions.contains(req.getRequestURI())
               || containsId(req) && checkInvoice(req) && checkCard(req) && checkLogbookEntry(req);
    }

    private static boolean containsId(ServletRequest req) {
        for (String param : PARAMS) {
            if (req.getParameter(param) != null) {
                return true;
            }
        }
        return false;
    }

    private boolean checkInvoice(ServletRequest req) {
        String invoiceIdStr = req.getParameter(INVOICE_ID);
        if (invoiceIdStr != null && !invoiceIdStr.isEmpty()) {
            Invoice invoice = invoiceDao.getByExternalInvoiceNumber(invoiceIdStr);
            BackendUser<? extends User> currentUser = authenticationService.getCurrentUser();
            return invoice.getUser().equals(currentUser.getUser());
        }
        return true;
    }

    private boolean checkLogbookEntry(ServletRequest req) {
        String logbookEntryIdStr = req.getParameter(LOGBOOK_ENTRY_ID);
        if (logbookEntryIdStr != null && !logbookEntryIdStr.isEmpty()) {
            Long logbookEntryId;
            try {
                logbookEntryId = Long.valueOf(logbookEntryIdStr);
            } catch (Exception ignored) {
                return false;
            }
            LogbookEntry logbookEntry = logbookEntryDao.getById(logbookEntryId);
            BackendUser<? extends User> currentUser = authenticationService.getCurrentUser();
            User user = currentUser.getUser();
            switch (user.getRole()) {
                case ROLE_AMATEUR:
                    return false;
                case ROLE_ATHLETE:
                    return false;
                case ROLE_DIVER:
                    return logbookEntry.getDiver().equals(user);
                case ROLE_FEDERATION_ADMIN:
                case ROLE_ADMIN:
                    return false;
            }

        }
        return true;
    }

    private boolean checkCard(ServletRequest req) {
        String cardIdStr = req.getParameter(CARD_ID);
        if (cardIdStr != null && !cardIdStr.isEmpty()) {
            Long cardId;
            try {
                cardId = Long.valueOf(cardIdStr);
            } catch (Exception ignored) {
                return false;
            }
            PersonalCard personalCard = personalCardDao.getById(cardId);
            BackendUser<? extends User> currentUser = authenticationService.getCurrentUser();
            User user = currentUser.getUser();
            switch (user.getRole()) {
                case ROLE_AMATEUR:
                    return false;
                case ROLE_ATHLETE:
                    return personalCard.getAthlete().equals(user);
                case ROLE_DIVER:
                    return personalCard.getDiver().equals(user);
                case ROLE_FEDERATION_ADMIN:
                case ROLE_ADMIN:
                    return false;
            }
        }
        return true;
    }

    @Required
    public void setExceptions(List<String> exceptions) {
        this.exceptions = exceptions;
    }

    @Required
    public void setFreePages(List<String> freePages) {
        this.freePages = freePages;
    }

    @Required
    public void setCmasBasicPages(List<String> cmasBasicPages) {
        this.cmasBasicPages = cmasBasicPages;
    }

    @Required
    public void setCmasBasicFreePages(List<String> cmasBasicFreePages) {
        this.cmasBasicFreePages = cmasBasicFreePages;
    }

    @Required
    public void setDemoPages(List<String> demoPages) {
        this.demoPages = demoPages;
    }

    @Required
    public void setGuestPages(List<String> guestPages) {
        this.guestPages = guestPages;
    }

    @Required
    public void setGoldPages(List<String> goldPages) {
        this.goldPages = goldPages;
    }
}
