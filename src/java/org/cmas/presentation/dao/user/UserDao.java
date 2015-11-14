package org.cmas.presentation.dao.user;

import org.cmas.presentation.entities.user.UserClient;
import org.cmas.presentation.model.user.UserSearchFormObject;
import org.cmas.util.dao.IdGeneratingDao;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public interface UserDao extends IdGeneratingDao<UserClient> {

    boolean isEmailUnique(String email);
    boolean isEmailUnique(String email, @Nullable Long id);

     /**
     * Ищем клиентов
     * @param form
     * @return
     */
    List<UserClient> searchUsers(UserSearchFormObject form);

    int getMaxCountSearchUsers(UserSearchFormObject form);

    /**
     * Ищем аккаунт по е-mail`у
     * @param email
     * @return
     */
    UserClient getByEmail(@NotNull String email);

    /**
     * Ищем аккаунт по коду для смены пароля
     * @param lostPasswdCode
     * @return
     */
    UserClient getBylostPasswdCode(@NotNull String lostPasswdCode);

   /**
     * Используется при смене e-mail клиента
     * Для подтверждения.
     * Ищется клиент с определенным md5
     * @param md5
     * @return
     */
    UserClient getUserChangedEmail(String md5);

    void evictUser(long userId);    
}
