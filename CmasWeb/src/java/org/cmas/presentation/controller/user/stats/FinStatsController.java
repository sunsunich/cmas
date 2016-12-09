package org.cmas.presentation.controller.user.stats;

import org.cmas.presentation.dao.billing.FinLogDao;
import org.cmas.presentation.entities.billing.FinLog;
import org.cmas.presentation.entities.user.BackendUser;
import org.cmas.presentation.model.user.fin.FinStatsFormObject;
import org.cmas.presentation.service.AuthenticationService;
import org.cmas.presentation.validator.HibernateSpringValidator;
import org.cmas.util.http.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.List;

@Controller
public class FinStatsController {


    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private FinLogDao finLogDao;

    @Autowired
    private HibernateSpringValidator validator;

    @ModelAttribute("user")
    public BackendUser getUser() {
        BackendUser user = authenticationService.getCurrentUser();
        if (user == null) {
            throw new BadRequestException();
        }
        return user;
    }

    @RequestMapping(value = "/secure/finstat.html", method = RequestMethod.GET)
    public ModelAndView showFinLog(
            @ModelAttribute("command") FinStatsFormObject finStatsFormObject
            , BindingResult result
    ) {
        BackendUser user = authenticationService.getCurrentUser();
        if (user == null) {
            throw new BadRequestException();
        }
        validator.validate(finStatsFormObject, result);
        if (result.hasErrors()) {
            biuldFinStatView(Collections.<FinLog>emptyList(), 0);
        }

        List<FinLog> finLogs = finLogDao.getByUser(user, finStatsFormObject);
        int count = finLogDao.countForUser(user, finStatsFormObject);
        return biuldFinStatView(finLogs, count);
    }

    private ModelAndView biuldFinStatView(List<FinLog> finLogs, int count) {
        ModelMap mm = new ModelMap();
        mm.addAttribute("finLogs", finLogs);
        mm.addAttribute("count", count);
        return new ModelAndView("secure/finstat", mm);
    }

}
