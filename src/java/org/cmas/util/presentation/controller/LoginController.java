package org.cmas.util.presentation.controller;

import org.cmas.util.http.Cookies;
import org.cmas.util.json.gson.GsonViewFactory;
import org.cmas.util.presentation.spring.LogoutEventBroadcaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.ui.AbstractProcessingFilter;
import org.springframework.security.ui.webapp.AuthenticationProcessingFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private GsonViewFactory gsonViewFactory;    

    @RequestMapping(value = "/login.html", method = RequestMethod.GET)
    public View showLogin(ModelMap model,
                          HttpSession sess,
                          @RequestParam(required = false, value = "login_error") Boolean hasError,
                          @RequestParam(required = false, value = "redirectUrl") String redirectUrl
    ) {
        if (hasError != null && hasError) {
            return gsonViewFactory.createErrorGsonView("cmas.badcredentials");
        }
        if(redirectUrl != null){
            return gsonViewFactory.createGsonView(new LoginRedirectGsonResponse(true, redirectUrl));
        }
        return gsonViewFactory. createSuccessGsonView();
    }


    @RequestMapping(value = "/login-redirect.html",method = RequestMethod.GET)
    public String loginRedirect(ModelMap model, HttpSession sess, HttpServletRequest request, HttpServletResponse response,
                            @RequestParam(required = false, value = "login_error") Boolean hasError) {
        if (hasError != null && hasError) {
            Throwable thread = (Throwable) sess.getAttribute(AbstractProcessingFilter.SPRING_SECURITY_LAST_EXCEPTION_KEY);
            if (thread != null) {
                model.addAttribute("loginError", thread.getMessage());
            }
            Object obj = sess.getAttribute(AuthenticationProcessingFilter.SPRING_SECURITY_LAST_USERNAME_KEY);
            if (obj != null) {
                model.addAttribute("lastLogin", obj);
            }
        }
        Cookie cookie = Cookies.rewriteValueInCookie(request.getCookies()
                , LogoutEventBroadcaster.AUTH_COOKIE_NAME
                , ""
                , 0);
        response.addCookie(cookie);
        return "login";
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showLogin(ModelMap model, HttpSession sess,
                            @RequestParam(required = false, value = "login_error") Boolean hasError) {
        if (hasError != null && hasError) {
            Throwable thread = (Throwable) sess.getAttribute(AbstractProcessingFilter.SPRING_SECURITY_LAST_EXCEPTION_KEY);
            if (thread != null) {
                model.addAttribute("loginError", thread.getMessage());
            }
            Object obj = sess.getAttribute(AuthenticationProcessingFilter.SPRING_SECURITY_LAST_USERNAME_KEY);
            if (obj != null) {
                model.addAttribute("lastLogin", obj);
            }
        }
        return "login";
    }

    /*
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView model = new ModelAndView("login");
        HttpSession sess = request.getSession();
        boolean hasError = request.getParameter("login_error") != null;
        if (hasError) {
            model.addObject("loginError", ((AuthenticationException) sess.getAttribute(AbstractProcessingFilter.ACEGI_SECURITY_LAST_EXCEPTION_KEY)).getMessage());
            model.addObject("lastLogin", sess.getAttribute(AuthenticationProcessingFilter.ACEGI_SECURITY_LAST_USERNAME_KEY));
        }
        return model;
    } */
}

