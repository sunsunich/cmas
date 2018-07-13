package org.cmas.util.presentation.controller;

import org.cmas.entities.diver.AreaOfInterest;
import org.cmas.presentation.dao.CountryDao;
import org.cmas.presentation.model.registration.DiverRegistrationFormObject;
import org.cmas.util.http.BadRequestException;
import org.cmas.util.http.Cookies;
import org.cmas.util.json.RedirectResponse;
import org.cmas.util.json.gson.GsonViewFactory;
import org.cmas.util.presentation.spring.LogoutEventBroadcaster;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.ui.AbstractProcessingFilter;
import org.springframework.security.ui.webapp.AuthenticationProcessingFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private GsonViewFactory gsonViewFactory;

    @Autowired
    private CountryDao countryDao;

    @RequestMapping(value = "/login-form.html", method = RequestMethod.GET)
    public ModelAndView showLoginForm(ModelMap model) {
        return buildLoginForm(model);
    }

    @NotNull
    private ModelAndView buildLoginForm(ModelMap model) {
        model.addAttribute("command", new DiverRegistrationFormObject());
        try {
            model.addAttribute("countries", countryDao.getAll());
            model.addAttribute("mode", "login");
            model.addAttribute("areas", AreaOfInterest.values());
        } catch (Exception e) {
            throw new BadRequestException(e);
        }
        return new ModelAndView("registration");
    }

    @RequestMapping(value = "/login.html", method = RequestMethod.GET)
    public View login(ModelMap model,
                      HttpSession sess,
                      @RequestParam(required = false, value = "login_error") Boolean hasError,
                      @RequestParam(required = false, value = "redirectUrl") String redirectUrl
    ) {
        if (hasError != null && hasError) {
            return gsonViewFactory.createErrorGsonView("validation.badCredentials");
        }
        if (redirectUrl != null) {
            return gsonViewFactory.createGsonView(new RedirectResponse(true, redirectUrl));
        }
        return gsonViewFactory.createSuccessGsonView();
    }


    @RequestMapping(value = "/login-redirect.html", method = RequestMethod.GET)
    public ModelAndView loginRedirect(ModelMap model, HttpSession sess, HttpServletRequest request, HttpServletResponse response,
                                      @RequestParam(required = false, value = "login_error") Boolean hasError) {
        if (hasError != null && hasError) {
            Throwable throwable
                    = (Throwable) sess.getAttribute(AbstractProcessingFilter.SPRING_SECURITY_LAST_EXCEPTION_KEY);
            if (throwable != null) {
                model.addAttribute("loginError", throwable.getMessage());
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
        return buildLoginForm(model);
    }

//    @RequestMapping(method = RequestMethod.GET)
//    public ModelAndView showLogin(ModelMap model, HttpSession sess,
//                                  @RequestParam(required = false, value = "login_error") Boolean hasError) {
//        if (hasError != null && hasError) {
//            Throwable
//                    thread
//                    = (Throwable) sess.getAttribute(AbstractProcessingFilter.SPRING_SECURITY_LAST_EXCEPTION_KEY);
//            if (thread != null) {
//                model.addAttribute("loginError", thread.getMessage());
//            }
//            Object obj = sess.getAttribute(AuthenticationProcessingFilter.SPRING_SECURITY_LAST_USERNAME_KEY);
//            if (obj != null) {
//                model.addAttribute("lastLogin", obj);
//            }
//        }
//        return new ModelAndView("redirect:/login.html");
//    }

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

