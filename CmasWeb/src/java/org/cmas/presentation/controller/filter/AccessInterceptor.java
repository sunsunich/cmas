package org.cmas.presentation.controller.filter;

import org.cmas.entities.PersonalCard;
import org.cmas.entities.User;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.logbook.LogbookEntry;
import org.cmas.presentation.dao.billing.InvoiceDao;
import org.cmas.presentation.dao.logbook.LogbookEntryDao;
import org.cmas.presentation.dao.user.PersonalCardDao;
import org.cmas.presentation.entities.billing.Invoice;
import org.cmas.presentation.entities.user.BackendUser;
import org.cmas.presentation.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
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

    /**
     * урлы-исключения, к которым не применяются проверки значений параметров.
     */
    protected List<String> exceptions = new ArrayList<>();
    protected List<String> freePages = new ArrayList<>();

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object controllerObj) throws Exception {
        // общая проверка параметров висит только на /secure/ части сайта.
        if (authenticationService.isAdmin()) {
            return true;
        }

        boolean forbidden = req.getRequestURI().startsWith("/secure/") &&
                            !commonValidation(req);
        if (forbidden) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN);
            log.warn("cannot access url" + req.getRequestURI());
            return false;
        } else {
            return true;
        }
    }

    /**
     * делает общие для всех запросов проверки
     *
     * @param req
     * @return false, если пользователя не пускаем
     */
    private boolean commonValidation(HttpServletRequest req) {
        if (freePages.contains(req.getRequestURI())) {
            return true;
        }

        if (!checkDiverPayed()) {
            return false;
        }

        if (exceptions.contains(req.getRequestURI())) {
            return true;
        }
        if (!containsId(req)) {
            return false;
        }
        return checkInvoice(req) && checkCard(req) && checkLogbookEntry(req);
    }

    /**
     * проверяет что реквест содержит хотя бы один непустой параметр с именем из некоторого множества.
     *
     * @param req
     * @return
     */
    private static boolean containsId(HttpServletRequest req) {
        for (String param : PARAMS) {
            if (req.getParameter(param) != null) {
                return true;
            }
        }
        return false;
    }

    private boolean checkDiverPayed() {
        if (!authenticationService.isDiver()) {
            return true;
        }
        BackendUser<? extends User> currentUser = authenticationService.getCurrentUser();
        if (((Diver) currentUser.getUser()).isHasPayed()) {
            return true;
        }

        return false;
    }


    /**
     * если в запросе есть параметр invoiceId, проверяем, что invoice с таким id принадлежит залогиненному пользователю
     */
    private boolean checkInvoice(HttpServletRequest req) {
        String invoiceIdStr = req.getParameter(INVOICE_ID);
        if (invoiceIdStr != null && !invoiceIdStr.isEmpty()) {
            Long invoiceId;
            try {
                invoiceId = Long.valueOf(invoiceIdStr);
            } catch (Exception ignored) {
                return false;
            }
            Invoice invoice = invoiceDao.getById(invoiceId);
            BackendUser<? extends User> currentUser = authenticationService.getCurrentUser();
            return invoice.getUser().equals(currentUser.getUser());
        }
        return true;
    }

    private boolean checkLogbookEntry(HttpServletRequest req) {
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

    private boolean checkCard(HttpServletRequest req) {
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
}
