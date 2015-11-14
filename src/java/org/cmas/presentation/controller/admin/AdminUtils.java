package org.cmas.presentation.controller.admin;

import org.cmas.presentation.model.Editable;
import org.cmas.presentation.service.EntityAddService;
import org.cmas.presentation.service.EntityDeleteService;
import org.cmas.presentation.service.EntityEditService;
import org.cmas.util.dao.HibernateDao;
import org.cmas.util.http.BadRequestException;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

public class AdminUtils {

    public static <E> ModelMap getModelMap(Editable<E> formObject, Boolean isSuccess) {
        ModelMap mm = new ModelMap();
        mm.addAttribute("command", formObject);
        mm.addAttribute("isSuccess", isSuccess == null ? false : isSuccess);
        return mm;
    }

    public static ModelMap delete(
            EntityDeleteService service
            , long itemId
    ) {
        service.delete(itemId);
        return new ModelMap();
    }

    public static <F extends Editable<E>, E> ModelAndView edit(
              EntityEditService<F, E> service
            , F formObject, BindingResult result
            , String errorJsp
            , String redirectUrl
    ) {
        service.edit(formObject, result);
        if (result.hasErrors()) {
            return new ModelAndView(errorJsp,getModelMap(formObject, false));
        } else {
            return new ModelAndView(redirectUrl);
        }
    }

    public static <F extends Editable<E>, E> ModelAndView add(
              EntityAddService<F, E> service
            , F formObject, BindingResult result
            , String errorJsp
            , String redirectUrl
    ) {
        service.add(formObject, result);
        if (result.hasErrors()) {
            return new ModelAndView(errorJsp,getModelMap(formObject, false));
        } else {
            return new ModelAndView(redirectUrl);
        }
    }

    public static <F extends Editable<E>, E> ModelMap load(
              HibernateDao<E> dao
            , F formObject
            , long id
            , Boolean isSuccess
    ) {
        E item = dao.getModel(id);
        if (item == null) {
            throw new BadRequestException();
        }
        formObject.transferFromEntity(item);

        return getModelMap(formObject, isSuccess);
    }
}
