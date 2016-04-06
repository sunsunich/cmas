package org.cmas.presentation.controller.user;

import org.cmas.entities.Role;
import org.cmas.entities.User;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.sport.Athlete;
import org.cmas.presentation.entities.user.BackendUser;
import org.cmas.presentation.model.user.EmailEditFormObject;
import org.cmas.presentation.model.user.PasswordEditFormObject;
import org.cmas.presentation.service.AuthenticationService;
import org.cmas.presentation.service.user.AthleteService;
import org.cmas.presentation.service.user.PasswordService;
import org.cmas.presentation.service.user.PasswordStrength;
import org.cmas.presentation.validator.HibernateSpringValidator;
import org.cmas.util.http.BadRequestException;
import org.cmas.util.http.HttpUtil;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**

 */
@SuppressWarnings("HardcodedFileSeparator")
@Controller
public class UserProfileController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private AthleteService userService;

    @Autowired
    private HibernateSpringValidator validator;

	@Autowired
    private PasswordService passwordService;

    @ModelAttribute("user")
    public BackendUser getUser() {
        BackendUser user = authenticationService.getCurrentUser();
        if (user == null) {
            throw new BadRequestException();
        }
        return user;
    }

    @ModelAttribute("diver")
    public Diver getCurrentDiver() {
        BackendUser<? extends User> user = getUser();
        Role role = user.getUser().getRole();
        Diver diver = null;
        if (role == Role.ROLE_DIVER_INSTRUCTOR || role == Role.ROLE_DIVER) {
            diver = (Diver) user.getUser();
        }
        if (diver == null) {
            throw new BadRequestException();
        }
        return diver;
    }

    @RequestMapping("/secure/profile/processEditUser.html")
    public ModelAndView processEditUser(
              HttpServletRequest request
            , @ModelAttribute("command") User formObject
            , BindingResult result
            , Model mm)
    {
        BackendUser user = authenticationService.getCurrentUser();
        if (user == null) {
            throw new BadRequestException();
        }
        validator.validate(formObject,result);
		if (result.hasErrors()) {
			return buidUserEditForm(mm, user, false);
		} else {
            userService.editUser((Athlete)user.getUser(), HttpUtil.getIP(request));
			return new ModelAndView("redirect:/secure/profile/getUser.html?isSuccess=true");
		}
    }

    @RequestMapping("/secure/profile/getUser.html")
    public ModelAndView getUser(
		    @RequestParam(required = false) Boolean isSuccess,
			Model model) {
        BackendUser user = authenticationService.getCurrentUser();
		if (user == null) {
            throw new BadRequestException();
        }
		model.addAttribute("command", user.getUser());
		return buidUserEditForm(model, user, isSuccess);
    }

	private ModelAndView buidUserEditForm(Model model, BackendUser user, @Nullable Boolean isSuccess) {
		if (isSuccess == null) {
			model.addAttribute("isSuccess", false);
		} else {
			model.addAttribute("isSuccess", isSuccess);
		}
		return new ModelAndView("/secure/userInfo");
	}

    /*
     * Загрузка данных для смены пароля
     */
    @RequestMapping(value = "/secure/passwdForm.html")
	public ModelAndView loadUserPasswd(Model mm) {
	   BackendUser user = authenticationService.getCurrentUser();
	   if (user == null) {
		   throw new BadRequestException();
	   }
	   PasswordEditFormObject passwd = new PasswordEditFormObject();
	   mm.addAttribute("command", passwd);
	   return buidPassChangeForm(mm, user, PasswordStrength.NONE);
   }

    /*
     * Submit формы редактирования пароля
     */
    @RequestMapping("/secure/processEditPasswd.html")
    public ModelAndView userEditPasswd(
              HttpServletRequest request
            , @ModelAttribute("command") PasswordEditFormObject formObject
            , BindingResult result
			, Model mm) {
        BackendUser user = authenticationService.getCurrentUser();
        if (user == null) {
            throw new BadRequestException();
        }
		PasswordStrength passwordStrength = passwordService.measurePasswordStrength(formObject.getPassword());
        userService.changePassword((Athlete)user.getUser(), formObject, result, HttpUtil.getIP(request));
        if (result.hasErrors()) {
            return buidPassChangeForm(mm, user, passwordStrength);
        } else {
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("passwordStrength", passwordStrength.name());
			return new ModelAndView("secure/passwdChangeSuccess", model);
		}
    }

	private static ModelAndView buidPassChangeForm(Model model, BackendUser user, PasswordStrength passwordStrength) {
		model.addAttribute("passwordStrength", passwordStrength.name());
		return new ModelAndView("secure/passwdForm");
	}

    /*
     * Форма по редактированию e-mail`а пользователя
     */
    @RequestMapping("/secure/editEmail.html")
    public ModelAndView loadUserEmail(Model model) {
        BackendUser user = authenticationService.getCurrentUser();
        if (user == null) {
            throw new BadRequestException();
        }
		EmailEditFormObject formObject = new EmailEditFormObject();
		model.addAttribute("command",formObject);
        return buidEmailChangeForm(model, user);
    }


    @RequestMapping("/secure/processEditEmail.html")
    public ModelAndView userEditEmail(@ModelAttribute("command") EmailEditFormObject formObject,
                                BindingResult result, Model mm) {
        BackendUser user = authenticationService.getCurrentUser();
        if (user == null) {
            throw new BadRequestException();
        }
        userService.changeEmail((Athlete)user.getUser(), formObject, result);
        if (result.hasErrors()) {
			return buidEmailChangeForm(mm, user);
        } else {
            return new ModelAndView("secure/emailChangeSent");
        }
    }

	private ModelAndView buidEmailChangeForm(Model model, BackendUser user) {
		return new ModelAndView("secure/emailForm");
	}
}