package org.cmas.util.presentation.spring;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.AuthenticationException;
import org.springframework.security.ui.AbstractProcessingFilter;
import org.springframework.security.ui.webapp.AuthenticationProcessingFilterEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class WithAjaxAuthenticationProcessingFilterEntryPoint extends AuthenticationProcessingFilterEntryPoint {

	public static final String AJAX_HEADER = "X-Requested-With";
    public static final String XMLHttpRequest = "XMLHttpRequest";
	public static final String XML_HTTP_REQUEST = "XML_HTTP_REQUEST";
    //private String redirectT
    private String ajaxLoginForm;

//    @Override
//	protected String determineUrlToUseForThisRequest (
//			final HttpServletRequest request, final HttpServletResponse response,
//			final AuthenticationException exception) {
//
//        String ajaxHeaderValue = request.getHeader(AJAX_HEADER);
//        if (ajaxHeaderValue != null && XMLHttpRequest.equals(ajaxHeaderValue)) {
//            HttpSession session = request.getSession(false);
//            if (session != null) {
//                session.removeAttribute(AbstractProcessingFilter.SPRING_SECURITY_SAVED_REQUEST_KEY);
//            }
//            return ajaxLoginForm;
//        }
//		return getLoginFormUrl();
//	}
	@Override
	protected String determineUrlToUseForThisRequest(
			final HttpServletRequest request, final HttpServletResponse response,
			final AuthenticationException exception) {

		String ajaxHeaderValue = request.getHeader(AJAX_HEADER);
		if (ajaxHeaderValue != null && XML_HTTP_REQUEST.equals(ajaxHeaderValue)) {
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
