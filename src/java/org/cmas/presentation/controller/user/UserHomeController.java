package org.cmas.presentation.controller.user;

import org.cmas.presentation.entities.billing.Invoice;
import org.cmas.presentation.entities.user.UserClient;
import org.cmas.presentation.model.billing.PaymentAddFormObject;
import org.cmas.presentation.service.AuthenticationService;
import org.cmas.presentation.service.billing.BillingService;
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

    @ModelAttribute("user")
    public UserClient getUser() {
        UserClient user = authenticationService.getCurrentUser();
        if (user == null) {
            throw new BadRequestException();
        }
        return user;
    }

    @RequestMapping(value = "/secure/index.html", method = RequestMethod.GET)
    public ModelAndView showIndex() throws IOException {
        ModelMap mm = new ModelMap();
        mm.addAttribute("isError", false);
        return new ModelAndView("secure/index", mm);
    }

    @RequestMapping("/secure/pay.html")
    @Transactional
    public ModelAndView pay(@ModelAttribute("command") PaymentAddFormObject fo, BindingResult errors) {

        ModelMap mm = new ModelMap();
        mm.addAttribute("command", fo);
        final UserClient user = authenticationService.getCurrentUser();
        if (StringUtil.isEmpty(fo.getAmount()) ||
                StringUtil.isEmpty(fo.getPaymentType())) {//|| StringUtil.isEmpty(fo.getCurrencyType())) {
            return new ModelAndView("/secure/pay", mm);
        }

        validator.validate(fo, errors);
        if (errors.hasErrors()) {
            return new ModelAndView("/secure/pay", mm);
        }

        Invoice invoice = billingService.createInvoice(fo, user);

        mm.addAttribute("invoiceId", invoice.getExternalInvoiceNumber());
        return new ModelAndView("redirect:/secure/billing/interkassa/accept.html", mm);
    }
}
