package org.cmas.presentation.service;

import org.cmas.presentation.entities.user.UserClient;
import org.cmas.util.presentation.CommonAuthentificationService;

public interface AuthenticationService extends CommonAuthentificationService<UserClient> {

    String getCurrentUserName();

    boolean isAdmin();

}
