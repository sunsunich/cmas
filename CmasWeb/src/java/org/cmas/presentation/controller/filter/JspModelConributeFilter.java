package org.cmas.presentation.controller.filter;

import org.cmas.backend.ImageStorageManager;
import org.cmas.presentation.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JspModelConributeFilter implements Filter {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ImageStorageManager imageStorageManager;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletRequest.setAttribute("webVersion", 48);
        servletRequest.setAttribute("userpicRoot", imageStorageManager.getUserpicRoot());
        servletRequest.setAttribute("cardsRoot", imageStorageManager.getCardImagesRoot());
        servletRequest.setAttribute("cardApprovalRequestImagesRoot", imageStorageManager.getCardApprovalRequestImagesRoot());
        servletRequest.setAttribute("logbookEntryImagesRoot", imageStorageManager.getLogbookEntryImagesRoot());
        String path = ((HttpServletRequest) servletRequest).getServletPath();
        if (!(path.startsWith("/secure/") || path.startsWith("/admin/"))) {

            String userName = authenticationService.getCurrentUserName();
            if (userName != null) {
                servletRequest.setAttribute("current_user_login", userName);
            }
        }

//        HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
//        if (    path.startsWith("/secure/")
//             && Cookies.getCookieValueByName(
//                  httpServletRequest.getCookies(), LogoutEventBroadcaster.AUTH_COOKIE_NAME) == null
//        ) {
//            @SuppressWarnings({"UnsecureRandomNumberGeneration"})
//            Cookie cookie = Cookies.createCookie(
//                      LogoutEventBroadcaster.AUTH_COOKIE_NAME
//                    , String.valueOf(Math.random())
//                    , LogoutEventBroadcaster.AUTH_COOKIE_AGE);
//            ((HttpServletResponse) servletResponse).addCookie(cookie);
//        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }
}
