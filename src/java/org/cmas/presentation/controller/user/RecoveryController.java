package org.cmas.presentation.controller.user;

import org.cmas.entities.sport.Athlete;
import org.cmas.presentation.dao.user.sport.AthleteDao;
import org.cmas.presentation.model.recovery.LostPasswordFormObject;
import org.cmas.presentation.model.recovery.PasswordChangeFormObject;
import org.cmas.presentation.model.user.UserDetails;
import org.cmas.presentation.service.CaptchaService;
import org.cmas.presentation.service.mail.MailService;
import org.cmas.presentation.service.user.PasswordService;
import org.cmas.presentation.service.user.PasswordStrength;
import org.cmas.presentation.validator.HibernateSpringValidator;
import org.cmas.presentation.validator.recovery.LostPasswordValidator;
import org.cmas.util.http.BadRequestException;
import org.cmas.util.text.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.providers.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

/**
 */

@Controller
public class RecoveryController {

    @Autowired
    private MailService mailer;

    @Autowired
    protected AthleteDao userDao;
    @Autowired
    private Md5PasswordEncoder passwordEncoder;
    @Autowired
    @Qualifier("lostPasswdValidator")
    private LostPasswordValidator passwordValidator;
    @Autowired
    private HibernateSpringValidator validator;
	@Autowired
	private CaptchaService captchaService;
	@Autowired
    private PasswordService passwordService;

    private final SecureRandom rnd = new SecureRandom();

    public static final String SALT = "Wfdf$%@T#@c)(";

	@RequestMapping(value = "/lostPasswdForm.html", method = RequestMethod.GET)
	public ModelAndView setupLostPasswdInitial(Model model) {
		LostPasswordFormObject formObject = new LostPasswordFormObject();
		model.addAttribute("command", formObject);
		return buildLostPasswordForm(model, true);
	}

	private ModelAndView buildLostPasswordForm(Model model, boolean isCaptchaCorrect) {
		model.addAttribute("captchaError", !isCaptchaCorrect);
        model.addAttribute("reCaptchaPublicKey", captchaService.getReCaptchaPublicKey());
		return new ModelAndView("lostPasswdForm");
	}

	@RequestMapping(value = "/lostPasswd.html", method = RequestMethod.POST)
    @Transactional
    public ModelAndView submitLostPasswd(HttpServletRequest servletRequest, HttpServletResponse servletResponse, @ModelAttribute("command") LostPasswordFormObject formObject,
                         BindingResult result, Model model) throws AddressException, UnsupportedEncodingException {
        passwordValidator.validate(formObject, result);
		boolean isCaptchaCorrect = captchaService.validateCaptcha(servletRequest, servletResponse);
		if (result.hasErrors() || !isCaptchaCorrect) {
			return buildLostPasswordForm(model, isCaptchaCorrect);
		} else {
            String email = StringUtil.trim(formObject.getEmail());
			@SuppressWarnings({"ConstantConditions"})
            Athlete user = userDao.getByEmail(email);
			// генерим код для смены пароля
			long rndNum = rnd.nextLong();
			String checkCode = passwordEncoder.encodePassword(user.getEmail() + rndNum, SALT);
			user.setLostPasswdCode(checkCode);
			userDao.updateModel(user);
			mailer.sendLostPasswd(user);
			model.addAttribute("user", user);
			return new ModelAndView("lostPasswdSuccess");
		}
	}

    @RequestMapping(value = "/toChangePasswd.html", method = RequestMethod.GET)
    public String prepareRegData(@RequestParam("code") final String code, Model mm) {
        Athlete user = userDao.getBylostPasswdCode(code);
        if (user == null) {
            return "redirect:/lostPasswdForm.html";
        }
		PasswordChangeFormObject formObject = new PasswordChangeFormObject();
		formObject.setCode(code);
		mm.addAttribute("command", formObject);
		return buildChangePasswordForm(mm, PasswordStrength.NONE);
    }

	private String buildChangePasswordForm(Model model, PasswordStrength passwordStrength) {
		model.addAttribute("passwordStrength", passwordStrength.name());
		return "changePasswdForm";
	}

	@RequestMapping(value = "/changePasswd.html", method = RequestMethod.POST)
    @Transactional
    public ModelAndView changePassword(@ModelAttribute("command") PasswordChangeFormObject formObject,
                         BindingResult result, Model mm) throws AddressException {
        
        String code = formObject.getCode();
        if (StringUtil.isEmpty(code)) {
            throw new BadRequestException();
        }               
        Athlete account = userDao.getBylostPasswdCode(code);
        if (account == null) {
            return new ModelAndView("redirect:/lostPasswdForm.html");
        }
		PasswordStrength passwordStrength = passwordService.measurePasswordStrength(formObject.getPassword());
        validator.validate(formObject, result);
        if (result.hasErrors()) { // show form view.
			return new ModelAndView(buildChangePasswordForm(mm, passwordStrength));
        }  else {
            account.setLostPasswdCode(null);
            String newPasswd = passwordEncoder.encodePassword(formObject.getPassword(), UserDetails.SALT);
            account.setPassword(newPasswd);
            userDao.updateModel(account);

			Map<String, Object> modelMap = new HashMap<String, Object>();
			modelMap.put("passwordStrength", passwordStrength.name());
			return new ModelAndView("passwdChangeSuccess", modelMap);
        }

    }
}
