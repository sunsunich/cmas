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

     /**
     * Ищем клиентов
     * @param form
     * @return
     */
    List<T> searchUsers(UserSearchFormObject form);

    int getMaxCountSearchUsers(UserSearchFormObject form);

    /**
     * Ищем аккаунт по е-mail`у
     * @param email
     * @return
     */
    T getByEmail(@NotNull String email);

    /**
     * Ищем аккаунт по коду для смены пароля
     * @param lostPasswdCode
     * @return
     */
    T getBylostPasswdCode(@NotNull String lostPasswdCode);

   /**
     * Используется при смене e-mail клиента
     * Для подтверждения.
     * Ищется клиент с определенным md5
     * @param md5
     * @return
     */
   T getUserChangedEmail(String md5);

    void evictUser(long userId);    
}
