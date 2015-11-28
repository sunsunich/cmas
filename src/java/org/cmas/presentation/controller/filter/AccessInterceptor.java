package org.cmas.presentation.controller.filter;

import org.cmas.entities.User;
import org.cmas.presentation.dao.billing.InvoiceDao;
import org.cmas.presentation.entities.billing.Invoice;
import org.cmas.presentation.entities.user.BackendUser;
import org.cmas.presentation.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class AccessInterceptor extends HandlerInterceptorAdapter {

    public static final String INVOICE_ID = "invoiceId";
    public static final String ORDER_ID = "orderId";

    private static final String[] PARAMS = {
            INVOICE_ID, ORDER_ID
    };

    @Autowired
    protected AuthenticationService authenticationService;
    @Autowired
    private InvoiceDao invoiceDao;

    private final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * урлы-исключения, к которым не применяются проверки значений параметров.
     */
    protected List<String> exceptions = new ArrayList<String>();

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object controllerObj) throws Exception {
        // общая проверка параметров висит только на /secure/ части сайта.
        if(authenticationService.isAdmin()){
            return true;
        }

        boolean forbidden =  req.getRequestURI().startsWith("/secure/") &&
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
        if (exceptions.contains(req.getRequestURI())) {
            return true;
        }
        if (!containsId(req)) {
            return false;
        }
        return checkInvoice(req);
    }

    /**
     * проверяет что реквест содержит хотя бы один непустой параметр с именем из некоторого множества.
     *
     * @param req
     * @return
     */
    private boolean containsId(HttpServletRequest req) {
        for (String param : PARAMS) {
            if (req.getParameter(param) != null){
                return true;
            }
        }
        return false;
    }

  
    /**
     * если в запросе есть параметр invoiceId, проверяем, что invoice с таким id принадлежит залогиненному пользователю
     */
    private boolean checkInvoice(HttpServletRequest req) {
        String invoiceIdStr = req.getParameter(INVOICE_ID);
        if (invoiceIdStr != null && invoiceIdStr.length() > 0) {
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

    public List<String> getExceptions() {
        return exceptions;
    }

   

    public void setExceptions(List<String> exceptions) {
        this.exceptions = exceptions;
    }
}
