package org.cmas.util.presentation.spring;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.AuthenticationException;
import org.springframework.security.ui.AbstractProcessingFilter;
import org.springframework.security.ui.webapp.AuthenticationProcessingFilterEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class WithAjaxAuthenticationProcessingFilterEntryPoint extends AuthenticationProcessingFilterEntryPoint {

    private static final String AJAX_HEADER = "X-Requested-With";
    private static final String XML_HTTP_REQUEST = "XMLHttpRequest";

    private String ajaxLoginForm;

    public static boolean isAjaxRequest(HttpServletRequest request) {
        String ajaxHeaderValue = request.getHeader(AJAX_HEADER);
        return ajaxHeaderValue != null && XML_HTTP_REQUEST.equals(ajaxHeaderValue);
    }

    @Override
    protected String determineUrlToUseForThisRequest(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
        if (isAjaxRequest(request)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.removeAttribute(AbstractProcessingFilter.SPRING_SECURITY_SAVED_REQUEST_KEY);
            }
            return ajaxLoginForm;
        }
        return getLoginFormUrl();
    }

    @Required
    public void setAjaxLoginForm(String ajaxLoginForm) {
        this.ajaxLoginForm = ajaxLoginForm;
    }


}
