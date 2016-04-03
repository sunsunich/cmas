package org.cmas.presentation.service;

import org.cmas.presentation.entities.user.BackendUser;
import org.cmas.util.presentation.CommonAuthentificationService;

public interface AuthenticationService extends CommonAuthentificationService<BackendUser> {

    String getCurrentUserName();

    boolean isAdmin();

    boolean isDiver();

}
