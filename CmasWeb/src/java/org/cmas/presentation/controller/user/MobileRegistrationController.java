package org.cmas.presentation.controller.user;

import com.google.myjson.Gson;
import org.cmas.entities.DeviceType;
import org.cmas.entities.User;
import org.cmas.entities.diver.AreaOfInterest;
import org.cmas.entities.diver.Diver;
import org.cmas.json.SimpleGsonResponse;
import org.cmas.presentation.dao.CountryDao;
import org.cmas.presentation.dao.user.DeviceDao;
import org.cmas.presentation.dao.user.sport.DiverDao;
import org.cmas.presentation.dao.user.sport.NationalFederationDao;
import org.cmas.presentation.entities.user.BackendUser;
import org.cmas.presentation.entities.user.Device;
import org.cmas.presentation.model.registration.DiverRegistrationFormObject;
import org.cmas.presentation.model.registration.FullDiverRegistrationFormObject;
import org.cmas.presentation.model.user.UserDetails;
import org.cmas.presentation.service.AuthenticationService;
import org.cmas.presentation.service.cards.PersonalCardService;
import org.cmas.presentation.service.user.AllUsersService;
import org.cmas.presentation.service.user.RegistrationService;
import org.cmas.remote.ErrorCodes;
import org.cmas.util.http.BadRequestException;
import org.cmas.util.json.JsonBindingResult;
import org.cmas.util.json.gson.GsonViewFactory;
import org.cmas.util.presentation.SpringRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.providers.encoding.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import java.util.Collections;
import java.util.HashMap;
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
    private PersonalCardService personalCardService;

    @Autowired
    private GsonViewFactory gsonViewFactory;

    @Autowired
    private AllUsersService allUsersService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping("/mobileRegisterForm.html")
    public ModelAndView registerWithCertificates(Model model) {
        model.addAttribute("command", new DiverRegistrationFormObject());
        model.addAttribute("countries", countryDao.getAll());
        model.addAttribute("federations", nationalFederationDao.getAll());
        model.addAttribute("areas", AreaOfInterest.values());
        return new ModelAndView("registrationMobile");
    }

    @RequestMapping(value = "/registerWithCertificates.html", method = RequestMethod.POST)
    public View registerWithCertificates(@RequestParam("registrationJson") String registrationJson) {
        log.error("registerWithCertificates called:" + registrationJson);
        FullDiverRegistrationFormObject formObject;
        try {
            formObject = gsonViewFactory.getCommonGson().fromJson(registrationJson, FullDiverRegistrationFormObject.class);
        } catch (Exception e) {
            throw new BadRequestException(e);
        }
        log.error("formObject=" + new Gson().toJson(formObject));
        Errors result = new MapBindingResult(new HashMap(), "logbookEntryJson");
        registrationService.validateFromMobile(formObject, result);
        if (result.hasErrors()) {
            return gsonViewFactory.createGsonView(new JsonBindingResult(result));
        }
        Locale locale = LocaleContextHolder.getLocale();
        formObject.setLocale(locale);
        registrationService.add(formObject);
        return gsonViewFactory.createGsonView(formObject);
    }

    @RequestMapping("/getSelf.html")
    public View getDiver(@RequestParam("token") String token) {
        Diver diver = diverDao.getDiverByToken(token);
        if (diver == null) {
            throw new BadRequestException();
        }
        personalCardService.setupDisplayCardsForDivers(Collections.singletonList(diver));
        return gsonViewFactory.createGsonView(diver);
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
