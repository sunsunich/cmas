package org.cmas.presentation.service.admin;

import org.cmas.entities.diver.Diver;
import org.cmas.presentation.entities.user.BackendUser;
import org.cmas.presentation.model.admin.AdminUserFormObject;
import org.cmas.presentation.model.admin.PasswordChangeFormObject;
import org.cmas.presentation.model.registration.RegistrationConfirmFormObject;


public interface AdminService {
     /****************************** user management ********/

    void cloneUser(Diver diver);

    /**
     * дизейблит клиента
     * @param userId
     */
    //void deactivateuser(Long userId);
    /**
     * енейблит клиента
     * @param userId
     */
    //void activateuser(Long userId);
    /**
     * изменяет данные клиента
     * @param formObject - новые данные клиента
     */
    void editUser(AdminUserFormObject formObject);
    /**
     * устанавливает клиенту/партнеру пароль
     * @param formObject - тут новый пароль
     */
    void changePassword(PasswordChangeFormObject formObject);


//    /***************************** registration management **********/
//    /**
//     * добавляет нового клиента через админский интерфейс на основании тех данных,
//     * которые пользователь сам ввел.
//     * @param data
//     * @return
//     */
//     user createuserFromRegistration(RegistrationEditData data);
//    /**
//     * Первая ступень прохождения регистрации на сайте.
//     * Пользователь вводит данные для регистрации
//     * @param formObject
//     * @return
//     */
//     userRegistration createRegistration(userRegistrationFormObject data);


    BackendUser processConfirmRegistration(RegistrationConfirmFormObject formObject, String ip);


}
