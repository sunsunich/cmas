package org.cmas.presentation.dao.user;

import org.cmas.entities.User;
import org.cmas.presentation.model.user.UserSearchFormObject;
import org.cmas.util.dao.IdGeneratingDao;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public interface UserDao<T extends User> extends IdGeneratingDao<T> {

    boolean isEmailUnique(String email);

    boolean isEmailUnique(String email, @Nullable Long id);

    List<T> searchUsers(UserSearchFormObject form);

    int getMaxCountSearchUsers(UserSearchFormObject form);

    T getByEmail(@NotNull String email);

    T getBylostPasswdCode(@NotNull String lostPasswdCode);

    T getUserChangedEmail(String md5);

    void evictUser(long userId);
}
