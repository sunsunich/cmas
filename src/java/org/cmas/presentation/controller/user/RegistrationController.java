package org.cmas.presentation.controller.user;

import org.cmas.entities.DeviceType;
import org.cmas.entities.User;
import org.cmas.entities.diver.Diver;
import org.cmas.i18n.LocaleResolverImpl;
import org.cmas.json.SimpleGsonResponse;
import org.cmas.presentation.dao.user.DeviceDao;
import org.cmas.presentation.dao.user.sport.DiverDao;
import org.cmas.presentation.entities.user.BackendUser;
import org.cmas.presentation.entities.user.Device;
import org.cmas.presentation.model.registration.DiverRegistrationFormObject;
import org.cmas.presentation.model.registration.DiverVerificationFormObject;
import org.cmas.presentation.model.registration.RegistrationConfirmFormObject;
import org.cmas.presentation.model.user.UserDetails;
import org.cmas.presentation.service.AuthenticationService;
import org.cmas.presentation.service.CaptchaService;
import org.cmas.presentation.service.admin.AdminService;
import org.cmas.presentation.service.mobile.DictionaryDataService;
import org.cmas.presentation.service.user.AllUsersService;
import org.cmas.presentation.service.user.RegistrationService;
import org.cmas.presentation.validator.HibernateSpringValidator;
import org.cmas.remote.ErrorCodes;
import org.cmas.util.Base64Coder;
import org.cmas.util.http.BadRequestException;
import org.cmas.util.http.HttpUtil;
import org.cmas.util.json.JsonBindingResult;
import org.cmas.util.json.gson.GsonViewFactory;
import org.cmas.util.presentation.SpringRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.providers.encoding.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import java.util.List;

/**
 */
@SuppressWarnings("HardcodedFileSeparator")
@Controller
public class RegistrationController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private AdminService adminService;

    @Autowired
    private DeviceDao deviceDao;

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private LocaleResolverImpl localeResolver;
    @Autowired
    @Qualifier("registrationService")
    private RegistrationService registrationService;

    @Autowired
    private DictionaryDataService dictionaryDataService;

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
    @Autowired
    private DiverDao diverDao;

    @RequestMapping("/diver-verification.html")
    public ModelAndView verifyDiver(Model model) {
        model.addAttribute("command", new DiverVerificationFormObject());
        return buildDiverVerificationForm(model, true, true);
    }

    private ModelAndView buildDiverVerificationForm(Model model, boolean isCaptchaCorrect, boolean hasUsers) {
        try {
            model.addAttribute("countries", dictionaryDataService.getCountries(0L));
        } catch (Exception e) {
            throw new BadRequestException(e);
        }
        model.addAttribute("captchaError", !isCaptchaCorrect);
        model.addAttribute("reCaptchaPublicKey", captchaService.getReCaptchaPublicKey());
        model.addAttribute("hasUsers", hasUsers);
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
            return buildDiverVerificationForm(model, isCaptchaCorrect, true);
        } else {  // submit form
            List<Diver> divers = diverDao.searchForVerification(formObject);
            if (divers.isEmpty()) {
                return buildDiverVerificationForm(model, true, false);
            }
            for (Diver diver : divers) {
                byte[] userpic = diver.getUserpic();
                if (userpic != null) {
                    diver.setUserpicBase64(Base64Coder.encodeString(userpic));
                }
            }
            model.addAttribute("divers", divers);
            return new ModelAndView("diversList");
        }
    }

    /**
     *
     */
    @RequestMapping("/diver-registration.html")
    public ModelAndView registerDiver(Model model) {
        model.addAttribute("command", new DiverRegistrationFormObject());
        try {
            model.addAttribute("countries", dictionaryDataService.getCountries(0L));
        } catch (Exception e) {
            throw new BadRequestException(e);
        }
        return new ModelAndView("registration");
    }

    @RequestMapping("/diver-registration-submit.html")
    public View registrationDiverAdd(
            @ModelAttribute("command") DiverRegistrationFormObject formObject
            , BindingResult result
    ) {
        registrationService.validate(formObject, result);
        if (result.hasErrors()) {
            return gsonViewFactory.createGsonView(new JsonBindingResult(result));
        } else {  // submit form
            formObject.setLocale(localeResolver.getCurrentLocale());
            Diver diver = registrationService.setupDiver(formObject);
            if (diver == null) {
                result.reject("error.sending.registration.email");
                return gsonViewFactory.createGsonView(new JsonBindingResult(result));
            }
            return gsonViewFactory.createSuccessGsonView();
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

    /**
     * добавленную в базу регистрацию превращаем в юзера.
     */
    @RequestMapping("/regConfirm.html")
    public ModelAndView userAddConfirm(
            HttpServletRequest request
            , @ModelAttribute RegistrationConfirmFormObject formObject
            , BindingResult result
    ) {
        registrationService.validateConfirm(formObject, result);
        if (!result.hasErrors()) {
            BackendUser user = adminService.processConfirmRegistration(formObject, HttpUtil.getIP(request));
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
                case ROLE_ADMIN:
                    break;
            }
            if (springRole == null) {
                throw new IllegalStateException();
            }
            authenticationService.loginAs(user, new SpringRole[]{springRole});
            return new ModelAndView(redirectUrl, null);
        }
        return new ModelAndView("redirect:/index.html", null);
    }
}
