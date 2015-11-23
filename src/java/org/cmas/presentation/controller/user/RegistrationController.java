package org.cmas.presentation.controller.user;

import org.cmas.i18n.LocaleResolverImpl;
import org.cmas.presentation.dao.user.AmateurDao;
import org.cmas.presentation.dao.user.sport.SportsmanDao;
import org.cmas.presentation.entities.user.BackendUser;
import org.cmas.presentation.entities.user.Registration;
import org.cmas.presentation.model.registration.RegistrationConfirmFormObject;
import org.cmas.presentation.service.AuthenticationService;
import org.cmas.presentation.service.admin.AdminService;
import org.cmas.presentation.service.user.PasswordService;
import org.cmas.presentation.service.user.PasswordStrength;
import org.cmas.presentation.service.user.RegistrationService;
import org.cmas.presentation.validator.ValidatorUtils;
import org.cmas.util.http.HttpUtil;
import org.cmas.util.json.gson.GsonViewFactory;
import org.cmas.util.presentation.SpringRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;

/**
 */
@Controller
public class RegistrationController {

	@Autowired
	protected SportsmanDao sportsmanDao;
	@Autowired
	protected AmateurDao amateurDao;
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

    @Autowired
    private GsonViewFactory gsonViewFactory;

	/**
	 *
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
		return new ModelAndView("registration");
	}

    /**
     * добавляем в базу регистрацию.
     *
     * @param formObject
     * @param result
     * @return
     */
    @RequestMapping(value = "/register-user-submit.html")
    public View registrationAdd(
            @ModelAttribute("command") Registration formObject
            , BindingResult result
            ) {
    //    PasswordStrength passwordStrength = passwordService.measurePasswordStrength(formObject.getPassword());
        registrationService.validate(formObject, result);
        if (result.hasErrors()) { // show form view.
            return gsonViewFactory.createErrorGsonView(
                    ValidatorUtils.getAllErrorCodes(result)
            );
        } else {  // submit form
            formObject.setLocale(localeResolver.getCurrentLocale());
            registrationService.add(formObject, result);
            return gsonViewFactory.createSuccessGsonView();
        }
    }

    //todo mobile registration - protect
//    @RequestMapping("/registerNewUser.html")
//    public View registerNewUser(@ModelAttribute("command") Registration formObject
//    ) {
//        BackendUser user = userDao.getByLogin(username);
//        if (user != null) {
//            return gsonViewFactory.createErrorGsonView(ErrorCodes.USER_ALREADY_EXISTS);
//        }
//        BackendUser newUser = new BackendUser();
//        newUser.setRole(SpringRole.ROLE_AMATEUR);
//        newUser.setUsername(username);
//        newUser.setPassword(
//                passwordEncoder.encodePassword(password, UserDetails.SALT)
//        );
//        try {
//            Long userId = (Long) userDao.save(newUser);
//            newUser.setId(userId);
//            return gsonViewFactory.createGsonView(new RegisterNewUserReply(userId));
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            return gsonViewFactory.createErrorGsonView(ErrorCodes.ERROR_WHILE_SAVING_USER);
//        }
//    }

	/**
	 * добавленную в базу регистрацию превращаем в юзера.
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
            BackendUser user = adminService.processConfirmRegistration(formObject, HttpUtil.getIP(request));
            SpringRole springRole = null;
            String redirectUrl = null;
            switch (user.getUser().getRole()) {
                case ROLE_AMATEUR:
                    springRole = SpringRole.ROLE_AMATEUR;
                    redirectUrl = "redirect:/secure/index.html";
                    break;
                case ROLE_SPORTSMAN:
                    springRole = SpringRole.ROLE_SPORTSMAN;
                    redirectUrl = "redirect:/sports/index.html";
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
