package org.cmas.presentation.controller.face;

import org.cmas.presentation.dao.CountryDao;
import org.cmas.util.http.BadRequestException;
import org.cmas.util.mail.MailerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@SuppressWarnings("HardcodedFileSeparator")
@Controller
public class HomePageController {

    @Autowired
    private CountryDao countryDao;

    @Autowired
    private MailerConfig mailerConfig;

    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public ModelAndView showHome(Model model) {
        ModelMap mm = new ModelMap();
        try {
            mm.addAttribute("countries", countryDao.getAll());
        } catch (Exception e) {
            throw new BadRequestException(e);
        }
        return new ModelAndView("index", mm);
    }

    @RequestMapping(value = "/faq.html", method = RequestMethod.GET)
    public ModelAndView faq() {
        ModelMap mm = new ModelMap();
        return new ModelAndView("faq", mm);
    }

    @RequestMapping(value = "/privacyPolicy.html", method = RequestMethod.GET)
    public ModelAndView privacyPolicy() {
        ModelMap mm = new ModelMap();
        return new ModelAndView("privacyPolicy", mm);
    }

    @RequestMapping(value = "/cookies.html", method = RequestMethod.GET)
    public ModelAndView cookies() {
        ModelMap mm = new ModelMap();
        mm.addAttribute("section", "cookies");
        return new ModelAndView("privacyPolicy", mm);
    }

    @RequestMapping(value = "/contacts.html", method = RequestMethod.GET)
    public ModelAndView contacts() {
        ModelMap mm = new ModelMap();
        mm.addAttribute("siteEmail", mailerConfig.getSupportAddr());
        return new ModelAndView("contacts", mm);
    }

    @RequestMapping(value = "/paymentInfo.html", method = RequestMethod.GET)
    public ModelAndView paymentInfo() {
        ModelMap mm = new ModelMap();
        return new ModelAndView("paymentInfo", mm);
    }
}

