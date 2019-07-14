package org.cmas.util.presentation.controller;

import org.cmas.entities.Role;
import org.cmas.entities.diver.AreaOfInterest;
import org.cmas.entities.diver.Diver;
import org.cmas.presentation.dao.CountryDao;
import org.cmas.presentation.dao.user.sport.DiverDao;
import org.cmas.presentation.entities.user.BackendUser;
import org.cmas.presentation.model.registration.DiverRegistrationFormObject;
import org.cmas.presentation.model.user.UserDetails;
import org.cmas.presentation.model.user.UserSearchFormObject;
import org.cmas.presentation.service.AuthenticationService;
import org.cmas.remote.ErrorCodes;
import org.cmas.util.StringUtil;
import org.cmas.util.http.BadRequestException;
import org.cmas.util.http.Cookies;
import org.cmas.util.json.JsonBindingResult;
import org.cmas.util.json.RedirectResponse;
import org.cmas.util.json.gson.GsonViewFactory;
import org.cmas.util.presentation.SpringRole;
import org.cmas.util.presentation.spring.LogoutEventBroadcaster;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.providers.encoding.PasswordEncoder;
import org.springframework.security.ui.AbstractProcessingFilter;
import org.springframework.security.ui.webapp.AuthenticationProcessingFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.annotation.Nullable;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

@Controller
public class LoginController {

    @Autowired
    private GsonViewFactory gsonViewFactory;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private DiverDao diverDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    @RequestMapping("/loginFederation.html")
    public View loginUser(
            @Nullable
            @RequestParam(value = "username", required = false)
                    String username,
            @Nullable
            @RequestParam(value = "password", required = false)
                    String password
    ) {
        Errors errors = new MapBindingResult(new HashMap(), "loginFederation");
        if(StringUtil.isTrimmedEmpty(username)) {
            errors.rejectValue("username", "validation.emailEmpty");
        }
        if(StringUtil.isTrimmedEmpty(password)){
            errors.rejectValue("password", "validation.passwordEmpty");
        }
        if (errors.hasErrors()) {
            return gsonViewFactory.createGsonView(new JsonBindingResult(errors));
        }

        UserSearchFormObject formObject = new UserSearchFormObject();
        formObject.setEmail(username);
        formObject.setUserRole(Role.ROLE_FEDERATION_ADMIN.name());
        formObject.setLimit(1);
        List<Diver> federations = diverDao.searchUsers(formObject);
        if (federations == null || federations.isEmpty()) {
            return gsonViewFactory.createErrorGsonView(ErrorCodes.NO_SUCH_USER);
        }
        Diver federation = federations.get(0);
        String encodedPass = passwordEncoder.encodePassword(password, UserDetails.SALT);
        if (!federation.getPassword().equals(encodedPass)) {
            return gsonViewFactory.createErrorGsonView(ErrorCodes.WRONG_PASSWORD);
        }
        authenticationService.loginAs(new BackendUser(federation),
                                      new SpringRole[]{SpringRole.fromRole(federation.getRole())}
        );
        return gsonViewFactory.createSuccessGsonView();
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

