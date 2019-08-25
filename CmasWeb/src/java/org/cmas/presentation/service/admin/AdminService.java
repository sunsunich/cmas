package org.cmas.presentation.service.admin;

import org.cmas.entities.diver.Diver;
import org.cmas.presentation.entities.user.BackendUser;
import org.cmas.presentation.entities.user.Registration;
import org.cmas.presentation.model.admin.AdminUserFormObject;
import org.cmas.presentation.model.admin.PasswordChangeFormObject;


public interface AdminService {
     /****************************** user management ********/

    void cloneUser(Diver diver);

    //void deactivateuser(Long userId);

    //void activateuser(Long userId);

    void editUser(AdminUserFormObject formObject);

    void changePassword(PasswordChangeFormObject formObject);


    BackendUser processConfirmRegistration(Registration registration, String ip);


}
