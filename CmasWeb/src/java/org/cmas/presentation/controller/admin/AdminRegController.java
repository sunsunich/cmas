package org.cmas.presentation.controller.admin;

import org.cmas.presentation.dao.user.RegistrationDao;
import org.cmas.presentation.entities.user.Registration;
import org.cmas.presentation.model.registration.RegistrationConfirmFormObject;
import org.cmas.presentation.service.admin.AdminService;
import org.cmas.presentation.service.user.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
public class AdminRegController {
    

    @Autowired
    private RegistrationDao registrationDao;

    @Autowired
    @Qualifier("registrationService")
    private RegistrationService registrationService;

    @Autowired
    private AdminService adminService;

    /**
     * Список клиентов к регистрации
     * @param mm
     * @return
     */
    @RequestMapping("/admin/registration/readyToCreate.html")
    public ModelMap readyToCreate(ModelMap mm) {
        List<Registration> userRegistrations = registrationService.getReadyToRegister();
        mm.addAttribute("regs", userRegistrations);
        return mm;
    }

    /**
     * Удаление регистрации
     *
     * @param id
     * @return
     */
    @RequestMapping("/admin/registration/deleteReg.html")
    public String deleteRegistration(@RequestParam("regId") Long id) {
        registrationService.delete(id);
        return "redirect:/admin/registration/readyToCreate.html";
    }

    @RequestMapping("/admin/registration/add.html")
    public String addRegistration(@RequestParam("regId") Long id) {
        Registration registration = registrationDao.getById(id);
        RegistrationConfirmFormObject formObject = new RegistrationConfirmFormObject();
        formObject.setRegId(registration.getNullableId());
        formObject.setSec(registration.getMd5());
        adminService.processConfirmRegistration(formObject,"");
        return "redirect:/admin/registration/readyToCreate.html";
    }

}

