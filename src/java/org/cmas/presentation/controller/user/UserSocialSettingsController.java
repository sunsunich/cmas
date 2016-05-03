package org.cmas.presentation.controller.user;

import org.cmas.presentation.service.AuthenticationService;
import org.cmas.presentation.service.user.DiverService;
import org.cmas.util.json.gson.GsonViewFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.View;

/**
 * Created on May 02, 2016
 *
 * @author Alexander Petukhov
 */
@SuppressWarnings("HardcodedFileSeparator")
@Controller
public class UserSocialSettingsController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private DiverService userService;

    @Autowired
    private GsonViewFactory gsonViewFactory;

    @RequestMapping("/secure/social/getTeam.html")
    public View getUser(Model model) {
        return gsonViewFactory.createSuccessGsonView();
    }
}
