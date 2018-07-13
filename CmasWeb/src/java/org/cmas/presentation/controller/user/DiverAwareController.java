package org.cmas.presentation.controller.user;

import org.cmas.entities.Role;
import org.cmas.entities.User;
import org.cmas.entities.diver.Diver;
import org.cmas.presentation.entities.user.BackendUser;
import org.cmas.presentation.service.AuthenticationService;
import org.cmas.util.http.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Created on Jul 12, 2018
 *
 * @author Alexander Petukhov
 */
public class DiverAwareController {

    @Autowired
    protected AuthenticationService authenticationService;

    @ModelAttribute("user")
    public BackendUser getUser() {
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
}
