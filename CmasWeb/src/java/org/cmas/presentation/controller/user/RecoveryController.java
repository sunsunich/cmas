package org.cmas.presentation.controller.user;

import org.cmas.entities.diver.Diver;
import org.cmas.presentation.dao.user.UserEventDao;
import org.cmas.presentation.dao.user.sport.DiverDao;
import org.cmas.presentation.entities.user.BackendUser;
import org.cmas.presentation.entities.user.UserEvent;
import org.cmas.presentation.entities.user.UserEventType;
import org.cmas.presentation.model.recovery.LostPasswordFormObject;
import org.cmas.presentation.model.recovery.PasswordChangeFormObject;
import org.cmas.presentation.model.user.UserDetails;
import org.cmas.presentation.service.AuthenticationService;
import org.cmas.presentation.service.CaptchaService;
import org.cmas.presentation.service.mail.MailService;
import org.cmas.presentation.service.user.AllUsersService;
import org.cmas.presentation.validator.HibernateSpringValidator;
import org.cmas.presentation.validator.recovery.LostPasswordValidator;
import org.cmas.util.StringUtil;
import org.cmas.util.http.BadRequestException;
import org.cmas.util.http.HttpUtil;
import org.cmas.util.json.JsonBindingResult;
import org.cmas.util.json.gson.GsonViewFactory;
import org.cmas.util.presentation.SpringRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.providers.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;

@SuppressWarnings("HardcodedFileSeparator")
@Controller
public class RecoveryController {

    @Autowired
    private MailService mailer;
    @Autowired
    protected DiverDao diverDao;
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
    private GsonViewFactory gsonViewFactory;
    @Autowired
    private AllUsersService allUsersService;
    @Autowired
    private UserEventDao userEventDao;
    @Autowired
    private AuthenticationService authenticationService;

    private final SecureRandom rnd = new SecureRandom();

    private static final String SALT = "Wfdf$%@T#@c)(";

    @RequestMapping(value = "/lostPasswordForm.html", method = RequestMethod.GET)
    public ModelAndView setupLostPasswdInitial(Model model) {
        LostPasswordFormObject formObject = new LostPasswordFormObject();
        model.addAttribute("command", formObject);
        return buildLostPasswordForm(model, true);
    }

    private ModelAndView buildLostPasswordForm(Model model, boolean isCaptchaCorrect) {
        model.addAttribute("captchaError", !isCaptchaCorrect);
        model.addAttribute("reCaptchaPublicKey", captchaService.getReCaptchaPublicKey());
        return new ModelAndView("lostPasswordForm");
    }

    @RequestMapping(value = "/lostPasswd.html", method = RequestMethod.POST)
    @Transactional
    public ModelAndView submitLostPasswd(HttpServletRequest servletRequest, HttpServletResponse servletResponse,
                                         @ModelAttribute("command") LostPasswordFormObject formObject,
                                         Errors result, Model model) throws AddressException, UnsupportedEncodingException {
        passwordValidator.validate(formObject, result);
        boolean isCaptchaCorrect = captchaService.validateCaptcha(servletRequest, servletResponse);
        if (result.hasErrors() || !isCaptchaCorrect) {
            return buildLostPasswordForm(model, isCaptchaCorrect);
        } else {
            String email = StringUtil.correctSpaceCharAndTrim(formObject.getEmail());
            Diver user = diverDao.getByEmail(email);
            long rndNum = rnd.nextLong();
            String checkCode = passwordEncoder.encodePassword(user.getEmail() + rndNum, SALT);
            user.setLostPasswdCode(checkCode);
            diverDao.updateModel(user);
            mailer.sendLostPasswd(user);
            model.addAttribute("user", user);
            return new ModelAndView("lostPasswdSuccess");
        }
    }

    @RequestMapping(value = "/toChangePasswd.html", method = RequestMethod.GET)
    public String toChangePasswd(@RequestParam("code") String code, Model model) {
        Diver user = diverDao.getBylostPasswdCode(code);
        if (user == null) {
            return "redirect:/lostPasswordForm.html";
        }
        PasswordChangeFormObject formObject = new PasswordChangeFormObject();
        formObject.setCode(code);
        model.addAttribute("command", formObject);
        return "changePasswdForm";
    }

    @RequestMapping(value = "/changePasswd.html", method = RequestMethod.POST)
    @Transactional
    public View changePassword(@ModelAttribute("command") PasswordChangeFormObject formObject,
                               BindingResult result, Model mm) throws AddressException {

        String code = formObject.getCode();
        if (StringUtil.isTrimmedEmpty(code)) {
            throw new BadRequestException();
        }
        Diver diver = diverDao.getBylostPasswdCode(code);
        if (diver == null) {
            throw new BadRequestException();
        }
        validator.validate(formObject, result);
        if (result.hasErrors()) { // show form view.
            return gsonViewFactory.createGsonView(new JsonBindingResult(result));
        } else {
            diver.setLostPasswdCode(null);
            String newPasswd = passwordEncoder.encodePassword(formObject.getPassword(), UserDetails.SALT);
            diver.setPassword(newPasswd);
            diverDao.updateModel(diver);
            return gsonViewFactory.createSuccessGsonView();
        }
    }

    @RequestMapping("/changeEmail.html")
    @Transactional
    public String changeEmailComplete(HttpServletRequest request, @RequestParam("sec") String sec, Model mm) {
        if (sec == null) {
            throw new IllegalArgumentException();
        }
        Diver diver = diverDao.getUserChangedEmail(sec);
        if (diver == null) {
            throw new BadRequestException();
        }
        mm.addAttribute("diver", diver);
        String newMail = diver.getNewMail();
        if (newMail == null) {
            throw new BadRequestException();
        }
        if (allUsersService.isEmailUnique(diver.getRole(), diver.getId(), newMail)) {
            userEventDao.save(new UserEvent(UserEventType.EMAIL_CHANGE,
                                            HttpUtil.getIP(request),
                                            diver.getEmail(), diver)
            );
            diver.setEmail(newMail);
            diver.setNewMail(null);
            diver.setMd5newMail(null);
            diverDao.updateModel(diver);
            authenticationService.loginAs(new BackendUser<>(diver),
                                          new SpringRole[]{SpringRole.fromRole(diver.getRole())});
            //email changed successfully
            return "changeEmail";
        } else {
            //email change failed
            return "changeEmailFailed";
        }
    }
}
