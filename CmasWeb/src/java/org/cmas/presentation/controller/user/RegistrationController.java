package org.cmas.presentation.controller.user;

import org.cmas.entities.cards.PersonalCard;
import org.cmas.entities.diver.AreaOfInterest;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.diver.DiverRegistrationStatus;
import org.cmas.presentation.controller.filter.AccessInterceptor;
import org.cmas.presentation.dao.CountryDao;
import org.cmas.presentation.dao.cards.PersonalCardDao;
import org.cmas.presentation.dao.user.RegistrationDao;
import org.cmas.presentation.dao.user.sport.DiverDao;
import org.cmas.presentation.entities.user.BackendUser;
import org.cmas.presentation.entities.user.Registration;
import org.cmas.presentation.model.recovery.PasswordChangeFormObject;
import org.cmas.presentation.model.registration.DiverRegistrationChooseFormObject;
import org.cmas.presentation.model.registration.DiverRegistrationDTO;
import org.cmas.presentation.model.registration.DiverRegistrationFormObject;
import org.cmas.presentation.model.registration.DiverVerificationFormObject;
import org.cmas.presentation.model.registration.FullDiverRegistrationFormObject;
import org.cmas.presentation.model.registration.RegistrationConfirmFormObject;
import org.cmas.presentation.model.user.UserDetails;
import org.cmas.presentation.service.AuthenticationService;
import org.cmas.presentation.service.CaptchaService;
import org.cmas.presentation.service.admin.AdminService;
import org.cmas.presentation.service.cards.PersonalCardService;
import org.cmas.presentation.service.user.RegistrationService;
import org.cmas.presentation.validator.HibernateSpringValidator;
import org.cmas.util.StringUtil;
import org.cmas.util.http.BadRequestException;
import org.cmas.util.http.HttpUtil;
import org.cmas.util.json.ImageUrlDTO;
import org.cmas.util.json.JsonBindingResult;
import org.cmas.util.json.gson.GsonViewFactory;
import org.cmas.util.presentation.SpringRole;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.providers.encoding.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 *
 */
@SuppressWarnings("HardcodedFileSeparator")
@Controller
public class RegistrationController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private RegistrationDao registrationDao;

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    @Qualifier("registrationService")
    private RegistrationService registrationService;

    @Autowired
    private CountryDao countryDao;

    @Autowired
    private GsonViewFactory gsonViewFactory;

    @Autowired
    private PersonalCardService personalCardService;

    @Autowired
    private PersonalCardDao personalCardDao;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CaptchaService captchaService;
    @Autowired
    private HibernateSpringValidator validator;
    @Autowired
    private DiverDao diverDao;

    @RequestMapping("/getCardImageUrl.html")
    public View getCardImageUrl(@RequestParam(AccessInterceptor.CARD_ID) long cardId) {
        PersonalCard personalCard = personalCardDao.getById(cardId);
        if (StringUtil.isTrimmedEmpty(personalCard.getImageUrl())) {
            personalCard = personalCardService.generateAndSaveCardImage(cardId);
        }
        String imageUrl = personalCard.getImageUrl();
        if (StringUtil.isTrimmedEmpty(imageUrl)) {
            return gsonViewFactory.createErrorGsonView("error.card.not.ready");
        } else {
            return gsonViewFactory.createGsonView(new ImageUrlDTO(true, imageUrl));
        }
    }

    @RequestMapping("/diver-verification.html")
    public ModelAndView verifyDiver(Model model, @ModelAttribute("command") DiverVerificationFormObject formObject) {
        //  model.addAttribute("command", new DiverVerificationFormObject());
        return buildDiverVerificationForm(model, true);
    }

    private ModelAndView buildDiverVerificationForm(
            Model model, boolean isCaptchaCorrect
    ) {
        return buildDiverVerificationForm(model, isCaptchaCorrect, false, Collections.<Diver>emptyList());
    }

    private ModelAndView buildDiverVerificationForm(
            Model model, boolean isCaptchaCorrect, boolean isSuccessFormSubmit, List<Diver> divers
    ) {
        try {
            model.addAttribute("countries", countryDao.getAll());
        } catch (Exception e) {
            throw new BadRequestException(e);
        }
        model.addAttribute("captchaError", !isCaptchaCorrect);
        model.addAttribute("reCaptchaPublicKey", captchaService.getReCaptchaPublicKey());
        model.addAttribute("isSuccessFormSubmit", isSuccessFormSubmit);
        model.addAttribute("divers", divers);
        return new ModelAndView("diverVerification");
    }

    @RequestMapping(value = "/diver-verification-submit.html", method = RequestMethod.POST)
    public ModelAndView diverVerificationSubmit(
            HttpServletRequest servletRequest, HttpServletResponse servletResponse,
            @ModelAttribute("command") DiverVerificationFormObject formObject,
            Errors result, Model model
    ) {
        validator.validate(formObject, result);
        boolean isCaptchaCorrect = captchaService.validateCaptcha(servletRequest, servletResponse);
        if (result.hasErrors() || !isCaptchaCorrect) {
            return buildDiverVerificationForm(model, isCaptchaCorrect);
        } else {  // submit form
            List<Diver> divers = diverDao.searchForVerification(formObject);
            personalCardService.setupDisplayCardsForDivers(divers);
            return buildDiverVerificationForm(model, true, true, divers);
        }
    }

    /**
     *
     */
    @RequestMapping("/diver-registration.html")
    public ModelAndView registerDiver(Model model) {
        model.addAttribute("command", new DiverRegistrationFormObject());
        model.addAttribute("countries", countryDao.getAll());
        model.addAttribute("areas", AreaOfInterest.values());
        model.addAttribute("mode", "registration");
        return new ModelAndView("registration");
    }

    @RequestMapping("/diver-registration-submit.html")
    public View registrationFirstStep(
            @ModelAttribute("command") DiverRegistrationFormObject formObject
            , BindingResult result
    ) {
        registrationService.validate(formObject, result);
        if (result.hasErrors()) {
            return gsonViewFactory.createGsonView(new JsonBindingResult(result));
        } else {  // submit form
            Locale locale = LocaleContextHolder.getLocale();
            formObject.setLocale(locale);
            List<DiverRegistrationDTO> diverRegistrationDTOS = registrationService.getDiversForRegistration(formObject);
            return gsonViewFactory.createGsonView(diverRegistrationDTOS);
        }
    }

    @RequestMapping("/diver-registration-choose.html")
    public View chooseRegistration(
            @ModelAttribute("command") DiverRegistrationChooseFormObject formObject
            , BindingResult result
    ) {
        validator.validate(formObject, result);
        if (result.hasErrors()) {
            return gsonViewFactory.createGsonView(new JsonBindingResult(result));
        }
        Diver diver = diverDao.getModel(Long.parseLong(formObject.getDiverId()));
        if (diver == null) {
            throw new BadRequestException();
        }
        if (diver.getPreviousRegistrationStatus() != DiverRegistrationStatus.NEVER_REGISTERED) {
            throw new BadRequestException();
        }
        String formObjectEmail = StringUtil.lowerCaseEmail(formObject.getEmail());
        if (!diver.getEmail().equals(formObjectEmail)) {
            result.rejectValue("email", "validation.registrationEmailMismatch");
            return gsonViewFactory.createGsonView(new JsonBindingResult(result));
        }
        diver.setAreaOfInterest(formObject.getAreaOfInterest());
        diverDao.updateModel(diver);
        registrationService.setupCMASDiver(diver, LocaleContextHolder.getLocale());
        return gsonViewFactory.createSuccessGsonView();
    }

    @RequestMapping("/diver-registration-create.html")
    public View registrationCreate(
            @ModelAttribute("command") FullDiverRegistrationFormObject formObject
            , BindingResult result
    ) {
        validator.validate(formObject, result);
        if (result.hasErrors()) {
            return gsonViewFactory.createGsonView(new JsonBindingResult(result));
        }
        registrationService.validateEmail(formObject, result);
        if (result.hasErrors()) {
            return gsonViewFactory.createGsonView(new JsonBindingResult(result));
        }
        Locale locale = LocaleContextHolder.getLocale();
        formObject.setLocale(locale);
        registrationService.add(formObject);
        return gsonViewFactory.createGsonView(formObject);
    }

    @RequestMapping("/regConfirm.html")
    public ModelAndView diverAddConfirm(
            @ModelAttribute RegistrationConfirmFormObject formObject
            , Errors errors
    ) {
        registrationService.validateConfirm(formObject, errors);
        if (errors.hasErrors()) {
            return new ModelAndView("redirect:/index.html", null);
        }
        return createSetPasswordForm(formObject.getSec());
    }

    @NotNull
    private static ModelAndView createSetPasswordForm(String sec) {
        PasswordChangeFormObject command = new PasswordChangeFormObject();
        command.setCode(sec);
        ModelMap model = new ModelMap();
        model.addAttribute("command", command);
        return new ModelAndView("setPasswordForm", model);
    }

    @RequestMapping("/setPasswordSubmit.html")
    public ModelAndView setPasswordSubmit(
            HttpServletRequest request
            , @ModelAttribute PasswordChangeFormObject formObject
            , Errors errors
    ) {
        validator.validate(formObject, errors);
        if (errors.hasErrors()) {
            return new ModelAndView("redirect:/index.html", null);
        }
        String code = formObject.getCode();
        Registration registration = registrationDao.getBySec(code);
        if (registration == null) {
            return new ModelAndView("redirect:/index.html", null);
        }
        registration.setPassword(passwordEncoder.encodePassword(formObject.getPassword(), UserDetails.SALT));
        BackendUser user = adminService.processConfirmRegistration(registration, HttpUtil.getIP(request));
        SpringRole springRole = null;
        String redirectUrl = null;
        switch (user.getUser().getRole()) {
            case ROLE_AMATEUR:
                springRole = SpringRole.ROLE_AMATEUR;
                redirectUrl = "redirect:/secure/index.html";
                break;
            case ROLE_ATHLETE:
                springRole = SpringRole.ROLE_ATHLETE;
                redirectUrl = "redirect:/secure/index.html";
                break;
            case ROLE_DIVER:
                springRole = SpringRole.ROLE_DIVER;
                redirectUrl = "redirect:/secure/firstLogin.html";
                break;
            case ROLE_FEDERATION_ADMIN:
                springRole = SpringRole.ROLE_FEDERATION_ADMIN;
                redirectUrl = "redirect:/fed/index.html";
                break;
            case ROLE_ADMIN:
                break;
        }
        if (springRole == null) {
            throw new IllegalStateException();
        }
        authenticationService.loginAs(user, new SpringRole[]{springRole});
        return new ModelAndView(redirectUrl, null);
    }
}
