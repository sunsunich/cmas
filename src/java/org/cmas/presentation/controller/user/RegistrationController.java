package org.cmas.presentation.controller.user;

import org.cmas.Globals;
import org.cmas.entities.Country;
import org.cmas.entities.DeviceType;
import org.cmas.entities.User;
import org.cmas.entities.diver.Diver;
import org.cmas.i18n.LocaleResolverImpl;
import org.cmas.json.SimpleGsonResponse;
import org.cmas.presentation.dao.CountryDao;
import org.cmas.presentation.dao.user.AmateurDao;
import org.cmas.presentation.dao.user.DeviceDao;
import org.cmas.presentation.dao.user.sport.AthleteDao;
import org.cmas.presentation.entities.user.BackendUser;
import org.cmas.presentation.entities.user.Device;
import org.cmas.presentation.entities.user.Registration;
import org.cmas.presentation.model.registration.DiverRegistrationFormObject;
import org.cmas.presentation.model.registration.RegistrationConfirmFormObject;
import org.cmas.presentation.model.user.UserDetails;
import org.cmas.presentation.service.AuthenticationService;
import org.cmas.presentation.service.admin.AdminService;
import org.cmas.presentation.service.mobile.DictionaryDataService;
import org.cmas.presentation.service.sports.NationalFederationService;
import org.cmas.presentation.service.user.AllUsersService;
import org.cmas.presentation.service.user.PasswordService;
import org.cmas.presentation.service.user.PasswordStrength;
import org.cmas.presentation.service.user.RegistrationService;
import org.cmas.presentation.validator.HibernateSpringValidator;
import org.cmas.remote.ErrorCodes;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

/**
 */
@Controller
public class RegistrationController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected AthleteDao athleteDao;
    @Autowired
    protected AmateurDao amateurDao;
    @Autowired
    private AdminService adminService;

    @Autowired
    private CountryDao countryDao;

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
    private NationalFederationService federationService;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private DictionaryDataService dictionaryDataService;

    @Autowired
    private HibernateSpringValidator validator;

    @Autowired
    private GsonViewFactory gsonViewFactory;

    @Autowired
    private AllUsersService allUsersService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * @param formObject
     * @param result
     * @param model
     * @return
     */
    @SuppressWarnings({"UnusedDeclaration"})
    @RequestMapping("/registration.html")
    public ModelAndView registerUser(@ModelAttribute("command") Registration formObject, BindingResult result, Model model) {
        model.addAttribute("command", new Registration());
        return buidAddRegistrationForm(model, PasswordStrength.NONE);
    }

    private ModelAndView buidAddRegistrationForm(Model model, PasswordStrength passwordStrength) {
        model.addAttribute("passwordStrength", passwordStrength.name());
        try {
            model.addAttribute("countries", dictionaryDataService.getCountries(0L));
            model.addAttribute("roles", dictionaryDataService.getRoles(0L));
        } catch (Exception e) {
            throw new BadRequestException(e);
        }
        return new ModelAndView("registration");
    }

    /**
     * добавляем в базу регистрацию.
     *
     * @param formObject
     * @param result
     * @return
     */
    @RequestMapping("/register-user-submit.html")
    public View registrationAdd(
            @ModelAttribute("command") Registration formObject
            , BindingResult result
    ) {
        //    PasswordStrength passwordStrength = passwordService.measurePasswordStrength(formObject.getPassword());
        registrationService.validate(formObject, result);
        if (formObject.isSkipFederationCheck() && result.hasFieldErrors()
            || !formObject.isSkipFederationCheck() && result.hasErrors()) {
            return gsonViewFactory.createGsonView(
                    new JsonBindingResult(result)
            );
        } else {  // submit form
            formObject.setLocale(localeResolver.getCurrentLocale());
            registrationService.add(formObject, result);
            return gsonViewFactory.createSuccessGsonView();
        }
    }


    @RequestMapping("/checkDiverRegistration.html")
    public View checkDiverRegistration(
            @ModelAttribute("command") DiverRegistrationFormObject formObject,
            BindingResult result
    ) {
        validator.validate(formObject, result);
        Country country = countryDao.getByCode(formObject.getCountry());
        if (country == null) {
            result.rejectValue("country", "validation.incorrectField");
        }
        if (result.hasFieldErrors()
            || result.hasErrors()) {
            return gsonViewFactory.createGsonView(
                    new JsonBindingResult(result)
            );
        }

        try {
            Diver diver = federationService.getDiver(
                    formObject.getFirstName(),
                    formObject.getLastName(),
                    Globals.getDTF().parse(formObject.getDob()),
                    country
            );
            return diver == null ? gsonViewFactory.createErrorGsonView(ErrorCodes.NO_SUCH_USER) :
                    gsonViewFactory.createSuccessGsonView();
        } catch (ParseException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * добавленную в базу регистрацию превращаем в юзера.
     *
     * @param request
     * @param formObject
     * @param result
     * @return
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
        if(user instanceof Diver){
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
     * Подтверждение пользователем смены e-mail`а
     * @param request
     * @param sec
     * @param mm
     * @return
     */
//	@RequestMapping("/changeEmail.html")
//	@Transactional
//	public String changeEmailComplete(HttpServletRequest request, @RequestParam("sec") final String sec, Model mm) {
//		if (sec == null) {
//			throw new IllegalArgumentException();
//		}
//		BackendUser user = userDao.getUserChangedEmail(sec);
//		if (user == null) {
//			throw new BadRequestException();
//		}
//		mm.addAttribute("user", user);
//		String newMail = user.getNewMail();
//		if (newMail == null) {
//			throw new BadRequestException();
//		}
//		if (userService.isEmailUnique(user, newMail)) {
//            userEventDao.save(new UserEvent(UserEventType.EMAIL_CHANGE, user, HttpUtil.getIP(request), user.getEmail()));
//			user.setEmail(newMail);
//			user.setNewMail(null);
//			user.setMd5newMail(null);
//			userDao.updateModel(user);
//            authenticationService.loginAs(user, new SpringRole[]{SpringRole.ROLE_AMATEUR});
//			//email changed successfully
//			return "changeEmail";
//		} else {
//			//email change failed
//			return "changeEmailFailed";
//		}
//	}
}
