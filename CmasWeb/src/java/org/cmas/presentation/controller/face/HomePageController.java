package org.cmas.presentation.controller.face;

import org.cmas.presentation.dao.CountryDao;
import org.cmas.util.http.BadRequestException;
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

    @RequestMapping(value = "/termsAndCond.html", method = RequestMethod.GET)
    public ModelAndView termsAndCond() {
        ModelMap mm = new ModelMap();
        return new ModelAndView("termsAndCond", mm);
    }
}

