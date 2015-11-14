package org.cmas.presentation.controller.face;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomePageController {


    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public ModelAndView showHome() {
        ModelMap mm = new ModelMap();

        return new ModelAndView("index", mm);
    }
 
}

