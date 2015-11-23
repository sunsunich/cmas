package org.cmas.presentation.service.user;

import org.cmas.entities.Role;
import org.jetbrains.annotations.Nullable;

/**
 * Created on Nov 22, 2015
 *
 * @author Alexander Petukhov
 */
public interface AllUsersService {

    boolean isEmailUnique(Role role, @Nullable Long userId, String email);
}
