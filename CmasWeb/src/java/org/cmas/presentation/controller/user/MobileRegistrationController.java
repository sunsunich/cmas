package org.cmas.presentation.controller.user;

import org.cmas.entities.DeviceType;
import org.cmas.entities.User;
import org.cmas.entities.diver.AreaOfInterest;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.diver.DiverLevel;
import org.cmas.entities.diver.DiverType;
import org.cmas.json.SimpleGsonResponse;
import org.cmas.presentation.controller.cards.CardDisplayManager;
import org.cmas.presentation.dao.CountryDao;
import org.cmas.presentation.dao.user.DeviceDao;
import org.cmas.presentation.dao.user.sport.DiverDao;
import org.cmas.presentation.dao.user.sport.NationalFederationDao;
import org.cmas.presentation.entities.user.BackendUser;
import org.cmas.presentation.entities.user.Device;
import org.cmas.presentation.model.cards.CardApprovalRequestMobileFormObject;
import org.cmas.presentation.model.registration.DiverRegistrationFormObject;
import org.cmas.presentation.model.registration.DiverVerificationAjaxFormObject;
import org.cmas.presentation.model.registration.FullDiverRegistrationFormObject;
import org.cmas.presentation.model.user.UserDetails;
import org.cmas.presentation.service.AuthenticationService;
import org.cmas.presentation.service.CaptchaService;
import org.cmas.presentation.service.cards.CardApprovalRequestService;
import org.cmas.presentation.service.cards.PersonalCardService;
import org.cmas.presentation.service.user.AllUsersService;
import org.cmas.presentation.service.user.RegistrationService;
import org.cmas.presentation.validator.HibernateSpringValidator;
import org.cmas.remote.ErrorCodes;
import org.cmas.util.StringUtil;
import org.cmas.util.http.BadRequestException;
import org.cmas.util.json.JsonBindingResult;
import org.cmas.util.json.gson.GsonViewFactory;
import org.cmas.util.presentation.SpringRole;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.providers.encoding.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 *
 */
@SuppressWarnings("HardcodedFileSeparator")
@Controller
public class MobileRegistrationController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private DeviceDao deviceDao;

    @Autowired
    private DiverDao diverDao;

    @Autowired
    private CountryDao countryDao;

    @Autowired
    private NationalFederationDao nationalFederationDao;

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    @Qualifier("registrationService")
    private RegistrationService registrationService;

    @Autowired
    private CardApprovalRequestService cardApprovalRequestService;

    @Autowired
    private CardDisplayManager cardDisplayManager;

    @Autowired
    private PersonalCardService personalCardService;

    @Autowired
    private GsonViewFactory gsonViewFactory;

    @Autowired
    private AllUsersService allUsersService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CaptchaService captchaService;
    @Autowired
    private HibernateSpringValidator validator;

    @RequestMapping("/mobileRegisterForm.html")
    public ModelAndView registerWithCertificates(Model model) {
        model.addAttribute("command", new DiverRegistrationFormObject());
        model.addAttribute("countries", countryDao.getAll());
        model.addAttribute("federations", nationalFederationDao.getAll());
        model.addAttribute("areas", AreaOfInterest.values());
        return new ModelAndView("registrationMobile");
    }

    private static SimpleGsonResponse toSimpleGsonResponse(Errors errors) {
        @Nullable
        String message;
        if (errors.hasFieldErrors()) {
            DefaultMessageSourceResolvable error = (DefaultMessageSourceResolvable) errors.getFieldErrors().get(0);
            message = error.getCode();
        } else if (errors.hasErrors()) {
            DefaultMessageSourceResolvable error = (DefaultMessageSourceResolvable) errors.getAllErrors().get(0);
            message = error.getCode();
        } else {
            message = null;
        }
        if (message == null) {
            return new SimpleGsonResponse(true, "");
        } else {
            return new SimpleGsonResponse(false, message);
        }
    }

    @RequestMapping(value = "/registerWithCertificates.html", method = RequestMethod.POST)
    public View registerWithCertificates(HttpServletRequest request) {
        String registrationJson = request.getParameter("registrationJson");
        //   log.error("registerWithCertificates called:" + registrationJson);
        FullDiverRegistrationFormObject formObject;
        try {
            formObject = gsonViewFactory.getCommonGson()
                                        .fromJson(registrationJson, FullDiverRegistrationFormObject.class);
        } catch (Exception e) {
            throw new BadRequestException(e);
        }
        //  log.error("formObject=" + new Gson().toJson(formObject));
        Errors result = new MapBindingResult(new HashMap(), "registrationJson");
        registrationService.validateFromMobile(formObject, result);
        if (result.hasErrors()) {
//            return gsonViewFactory.createGsonView(new JsonBindingResult(result));
            return gsonViewFactory.createGsonView(toSimpleGsonResponse(result));
        }
        Locale locale = LocaleContextHolder.getLocale();
        formObject.setLocale(locale);
        registrationService.add(formObject);
        return gsonViewFactory.createSuccessGsonView();
    }

    @RequestMapping("/mobileCarForm.html")
    public ModelAndView cardApprovalRequest(Model model) {
        model.addAttribute("command", new CardApprovalRequestMobileFormObject());
        model.addAttribute("countries", countryDao.getAll());
        model.addAttribute("federations", nationalFederationDao.getAll());
        model.addAttribute("diverTypes", DiverType.values());
        model.addAttribute("diverLevels", DiverLevel.values());
        model.addAttribute("cardGroups", cardDisplayManager.getPersonalCardGroups());
        return new ModelAndView("carMobile");
    }

    @RequestMapping(value = "/submitCertificateApprovalRequest.html", method = RequestMethod.POST)
    public View submitCertificateApprovalRequest(HttpServletRequest request) {
        String token = request.getHeader("CMAS_AUTH_TOKEN");
        Diver diver = diverDao.getDiverByToken(token);
        if (diver == null) {
            return gsonViewFactory.createErrorGsonView("validation.tokenUnknown");
        }
        String requestJson = request.getParameter("requestJson");
        //   log.error("registerWithCertificates called:" + requestJson);
        CardApprovalRequestMobileFormObject formObject;
        try {
            formObject = gsonViewFactory.getCommonGson()
                                        .fromJson(requestJson, CardApprovalRequestMobileFormObject.class);
        } catch (Exception e) {
            throw new BadRequestException(e);
        }
        //  log.error("formObject=" + new Gson().toJson(formObject));
        Errors result = new MapBindingResult(new HashMap(), "requestJson");
        cardApprovalRequestService.processCardApprovalRequestFromMobile(result, formObject, diver);
        if (result.hasErrors()) {
//            return gsonViewFactory.createGsonView(new JsonBindingResult(result));
            return gsonViewFactory.createGsonView(toSimpleGsonResponse(result));
        }
        return gsonViewFactory.createSuccessGsonView();
    }

    @RequestMapping("/getSelf.html")
    public View getDiver(@RequestParam("token") String token) {
        Diver diver = diverDao.getDiverByToken(token);
        if (diver == null) {
            return gsonViewFactory.createErrorGsonView("validation.tokenUnknown");
        }
        personalCardService.setupDisplayCardsForDivers(Collections.singletonList(diver));
        return gsonViewFactory.createSecureDiverView(diver);
    }

    @RequestMapping(value = "/diver-verification-ajax.html", method = RequestMethod.POST)
    public View diverVerificationAjax(
            HttpServletRequest servletRequest, HttpServletResponse servletResponse,
            @ModelAttribute("command") DiverVerificationAjaxFormObject formObject,
            Errors result
    ) {
        validator.validate(formObject, result);
        boolean isCaptchaCorrect = captchaService.validateCaptcha(servletRequest, servletResponse);
        if (!isCaptchaCorrect) {
            result.reject("validation.captchaError");
        }
        if (result.hasErrors()) {
            return gsonViewFactory.createGsonView(new JsonBindingResult(result));
        } else {
            List<Diver> divers;
            String universalCmasId = formObject.getUniversalCmasId();
            if (StringUtil.isTrimmedEmpty(universalCmasId)) {
                divers = diverDao.searchForVerification(formObject);
            } else {
                Diver diver = diverDao.getByPrimaryCardNumber(universalCmasId);
                if (diver == null) {
                    return gsonViewFactory.createErrorGsonView("validation.certificateUnknown");
                }
                divers = Collections.singletonList(diver);
            }
            personalCardService.setupDisplayCardsForDivers(divers);
            return gsonViewFactory.createDiverVerificationGsonView(divers);
        }
    }

    @RequestMapping("/loginUser.html")
    public View loginUser(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("deviceType") String deviceType,
            @RequestParam("deviceId") String deviceId,
            @RequestParam("pushServiceRegId") String pushServiceRegId
    ) {
        User user = allUsersService.getByEmail(username);
        if (user == null) {
            return gsonViewFactory.createErrorGsonView(ErrorCodes.NO_SUCH_USER);
        }
        String encodedPass = passwordEncoder.encodePassword(password, UserDetails.SALT);
        if (!user.getPassword().equals(encodedPass)) {
            return gsonViewFactory.createErrorGsonView(ErrorCodes.WRONG_PASSWORD);
        }

        SimpleGsonResponse simpleGsonResponse = registerDevice(deviceType, deviceId, pushServiceRegId, user);

        authenticationService.loginAs(new BackendUser(user),
                                      new SpringRole[]{SpringRole.fromRole(user.getRole())}
        );
        if (user instanceof Diver) {
            Diver diver = (Diver) user;
            return gsonViewFactory.createGsonView(diver);
        }
        return gsonViewFactory.createGsonView(user);
    }

    @RequestMapping("/secure/registerDevice.html")
    public View registerDevice(
            @RequestParam("deviceType") String deviceType,
            @RequestParam("deviceId") String deviceId,
            @RequestParam("pushServiceRegId") String pushServiceRegId
    ) {
        BackendUser user = authenticationService.getCurrentUser();
        if (user == null) {
            return gsonViewFactory.createErrorGsonView(ErrorCodes.NO_SUCH_USER);
        }

        SimpleGsonResponse simpleGsonResponse = registerDevice(deviceType, deviceId, pushServiceRegId, user.getUser());
        return gsonViewFactory.createGsonView(simpleGsonResponse);
    }

    //TODO device table locking
    private SimpleGsonResponse registerDevice(String deviceType,
                                              String deviceId,
                                              String pushServiceRegId,
                                              User user) {
        DeviceType deviceTypeEnum;
        try {
            deviceTypeEnum = DeviceType.valueOf(deviceType);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new SimpleGsonResponse(false, ErrorCodes.UNSUPPORTED_DEVICE_TYPE);
        }
        try {
            Device device = deviceDao.getByClientDeviceId(deviceId);
            if (device == null) {
                Device newDevice = new Device(
                        deviceTypeEnum, deviceId, pushServiceRegId, user
                );
                deviceDao.save(newDevice);

            } else {
                device.setPushServiceRegId(pushServiceRegId);
                deviceDao.updateModel(device);
            }
            return new SimpleGsonResponse(true, "");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new SimpleGsonResponse(false, ErrorCodes.ERROR_REGISTERING_DEVICE);
        }
    }

    //TODO device table locking
    @RequestMapping("/secure/unregisterDevice.html")
    public View unregisterDevice(
            @RequestParam("deviceId") String deviceId,
            @RequestParam("pushServiceRegId") String pushServiceRegId
    ) {
        try {
            Device device = deviceDao.getByClientDeviceId(deviceId);
            if (device != null) {
                if (device.getPushServiceRegId().equals(pushServiceRegId)) {
                    deviceDao.deleteModel(device);
                }
            }
            return gsonViewFactory.createSuccessGsonView();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return gsonViewFactory.createErrorGsonView(ErrorCodes.ERROR_UNREGISTERING_DEVICE);
        }
    }
}
