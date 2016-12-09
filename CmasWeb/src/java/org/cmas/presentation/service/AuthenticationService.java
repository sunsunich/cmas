package org.cmas.presentation.service;

import org.cmas.presentation.entities.user.BackendUser;
import org.cmas.util.presentation.CommonAuthenticationService;

public interface AuthenticationService extends CommonAuthenticationService<BackendUser> {

    String getCurrentUserName();

    boolean isAdmin();

    boolean isDiver();

}
