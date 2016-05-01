package org.cmas.presentation.controller.user;

import org.cmas.entities.Role;
import org.cmas.entities.User;
import org.cmas.entities.diver.Diver;
import org.cmas.presentation.dao.user.sport.DiverDao;
import org.cmas.presentation.entities.billing.Invoice;
import org.cmas.presentation.entities.user.BackendUser;
import org.cmas.presentation.model.billing.PaymentAddFormObject;
import org.cmas.presentation.service.AuthenticationService;
import org.cmas.presentation.service.billing.BillingService;
import org.cmas.presentation.service.user.RegistrationService;
import org.cmas.presentation.validator.HibernateSpringValidator;
import org.cmas.util.http.BadRequestException;
import org.cmas.util.text.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Date;

/**

 */
@Controller
public class UserHomeController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private HibernateSpringValidator validator;

    @Autowired
    private BillingService billingService;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private DiverDao diverDao;

    @ModelAttribute("user")
    public BackendUser<? extends User> getUser() {
        BackendUser<? extends User> user = authenticationService.getCurrentUser();
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
        if (role == Role.ROLE_DIVER) {
            diver = (Diver) user.getUser();
        }
        if (diver == null) {
            throw new BadRequestException();
        }
        return diver;
    }

    @RequestMapping(value = "/secure/index.html", method = RequestMethod.GET)
    public ModelAndView showIndex() throws IOException {
        Diver diver = getCurrentDiver();
        if (diver.isHasPayed()) {
            if (diver.getPrimaryPersonalCard() == null) {
                diver.setDateReg(new Date());
                registrationService.createDiverPrimaryCard(diver);
            }
            return new ModelAndView("redirect:/secure/profile/getUser.html");
        } else {
            return new ModelAndView("redirect:/secure/welcome.html");
        }
    }

    @RequestMapping(value = "/secure/welcome.html", method = RequestMethod.GET)
    public ModelAndView showWelcome() throws IOException {
        Diver diver = getCurrentDiver();
        boolean isFree = registrationService.isFreeRegistration(diver);
        if (isFree) {
            diver.setHasPayed(true);
            diverDao.updateModel(diver);
        }
        ModelMap mm = new ModelMap();
        mm.addAttribute("isFree", isFree);
        return new ModelAndView("/secure/welcome", mm);
    }

    @RequestMapping(value = "/secure/welcome-continue.html", method = RequestMethod.GET)
    public ModelAndView continueWelcome() throws IOException {
        Diver diver = getCurrentDiver();
        registrationService.createDiverPrimaryCard(diver);
        return new ModelAndView("redirect:/secure/profile/getUser.html");
    }


    @RequestMapping("/secure/pay.html")
    @Transactional
    public ModelAndView pay(@ModelAttribute("command") PaymentAddFormObject fo, BindingResult errors) {

        ModelMap mm = new ModelMap();
        mm.addAttribute("command", fo);
        final BackendUser<? extends User> user = authenticationService.getCurrentUser();
        if (user == null) {
            throw new BadRequestException();
        }
        if (StringUtil.isEmpty(fo.getAmount()) ||
            StringUtil.isEmpty(fo.getPaymentType())) {//|| StringUtil.isEmpty(fo.getCurrencyType())) {
            return new ModelAndView("/secure/pay", mm);
        }

        validator.validate(fo, errors);
        if (errors.hasErrors()) {
            return new ModelAndView("/secure/pay", mm);
        }

        Invoice invoice = billingService.createInvoice(fo, user.getUser());

        mm.addAttribute("invoiceId", invoice.getExternalInvoiceNumber());
        return new ModelAndView("redirect:/secure/billing/interkassa/accept.html", mm);
    }
}
