package org.cmas.presentation.service.user;

import org.cmas.entities.Role;
import org.cmas.entities.User;

import javax.annotation.Nullable;

/**
 * Created on Nov 22, 2015
 *
 * @author Alexander Petukhov
 */
public interface AllUsersService {

    boolean isEmailUnique(Role role, @Nullable Long userId, String email);

    @Nullable
    User getByEmail(String email);
}
