package org.cmas.presentation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;


@Controller
public class ErrorHandlerController {

   
    @RequestMapping("/404.html")
    public ModelAndView getStat(HttpServletResponse res, Model mm) {

        res.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return new ModelAndView("404");
    }

}
