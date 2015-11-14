package org.cmas.presentation.controller.user;

import org.cmas.i18n.LocaleResolverImpl;
import org.cmas.presentation.dao.user.UserDao;
import org.cmas.presentation.dao.user.UserEventDao;
import org.cmas.presentation.entities.user.Registration;
import org.cmas.presentation.entities.user.UserClient;
import org.cmas.presentation.entities.user.UserEvent;
import org.cmas.presentation.entities.user.UserEventType;
import org.cmas.presentation.model.registration.RegistrationAddFormObject;
import org.cmas.presentation.model.registration.RegistrationConfirmFormObject;
import org.cmas.presentation.service.AuthenticationService;
import org.cmas.presentation.service.admin.AdminService;
import org.cmas.presentation.service.user.PasswordService;
import org.cmas.presentation.service.user.PasswordStrength;
import org.cmas.presentation.service.user.RegistrationService;
import org.cmas.presentation.service.user.UserService;
import org.cmas.util.http.BadRequestException;
import org.cmas.util.http.HttpUtil;
import org.cmas.util.json.gson.GsonViewFactory;
import org.cmas.util.presentation.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 */
@Controller
public class RegistrationController {

	@Autowired
	@Qualifier("userService")
	private UserService userService;

	@Autowired
	protected UserDao userDao;
    @Autowired
    private UserEventDao userEventDao;
	@Autowired
    private AdminService adminService;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private LocaleResolverImpl localeResolver;
    @Autowired
    @Qualifier("registrationService")
    private RegistrationService registrationService;

    @Autowired
    private PasswordService passwordService;

	public static final String DEFAULT_ZONE = "Europe/Moscow";

    @Autowired
    private GsonViewFactory jsonFactory;

	/**
	 *
     * @param formObject
     * @param result
     * @param model
     * @return
     */
	@SuppressWarnings({"UnusedDeclaration"})
    @RequestMapping("/registration.html")
	public ModelAndView registerUser(@ModelAttribute("command") RegistrationAddFormObject formObject, BindingResult result, Model model) {
        model.addAttribute("command", new RegistrationAddFormObject());
		return buidAddRegistrationForm(model, PasswordStrength.NONE);
	}

    private ModelAndView buidAddRegistrationForm(Model model, PasswordStrength passwordStrength) {
		model.addAttribute("passwordStrength", passwordStrength.name());
		return new ModelAndView("registration");
	}

    /**
     * добавляем в базу регистрацию.
     * @param formObject
     * @param result
     * @param model
     * @return
     */
    @RequestMapping(value = "/register-user-submit.html", method = RequestMethod.POST)
    public ModelAndView registrationAdd(
              @ModelAttribute("command") RegistrationAddFormObject formObject
            , BindingResult result
            , Model model) {
        PasswordStrength passwordStrength = passwordService.measurePasswordStrength(formObject.getPassword());
        registrationService.validate(formObject, result);
        if (result.hasErrors()) { // show form view.
            return buidAddRegistrationForm(model, passwordStrength);
        } else {  // submit form
            formObject.setLocale(localeResolver.getCurrentLocale());
            Registration registration = registrationService.add(formObject, result);


            Map<String, Object> modelMap = new HashMap<String, Object>();
            modelMap.put("email", registration.getEmail());
//			if (returnAddress != null) {
//				model.put("returnAddress", returnAddress);
//			}
            modelMap.put("passwordStrength", passwordStrength.name());
            return new ModelAndView("registrationSuccess", modelMap);
        }
    }

	/**
	 * добавленную в базу регистрацию превращаем в клиента.
     * @param request
     * @param formObject
     * @param result
     * @return
     */
	@RequestMapping(value = "/regConfirm.html")
	public ModelAndView userAddConfirm(
              HttpServletRequest request
            , @ModelAttribute RegistrationConfirmFormObject formObject
            , BindingResult result
    ) {
		registrationService.validateConfirm(formObject, result);
		if (!result.hasErrors()) {
            UserClient user = adminService.processConfirmRegistration(formObject, HttpUtil.getIP(request));
			authenticationService.loginAs(user, new Role[]{Role.ROLE_USER});
			return new ModelAndView("redirect:/secure/index.html", null);
		}
		return new ModelAndView("redirect:/index.html", null);
	}


	/**
	 * Подтверждение пользователем смены e-mail`а
     * @param request
     * @param sec
     * @param mm
     * @return
     */
	@RequestMapping("/changeEmail.html")
	@Transactional
	public String changeEmailComplete(HttpServletRequest request, @RequestParam("sec") final String sec, Model mm) {
		if (sec == null) {
			throw new IllegalArgumentException();
		}
		UserClient user = userDao.getUserChangedEmail(sec);
		if (user == null) {
			throw new BadRequestException();
		}
		mm.addAttribute("user", user);
		String newMail = user.getNewMail();
		if (newMail == null) {
			throw new BadRequestException();
		}
		if (userService.isEmailUnique(user, newMail)) {
            userEventDao.save(new UserEvent(UserEventType.EMAIL_CHANGE, user, HttpUtil.getIP(request), user.getEmail()));
			user.setEmail(newMail);
			user.setNewMail(null);
			user.setMd5newMail(null);
			userDao.updateModel(user);
            authenticationService.loginAs(user, new Role[]{Role.ROLE_USER});
			//email changed successfully
			return "changeEmail";
		} else {
			//email change failed
			return "changeEmailFailed";
		}
	}
}
