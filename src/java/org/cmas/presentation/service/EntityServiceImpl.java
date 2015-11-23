package org.cmas.presentation.service;

import org.cmas.presentation.validator.HibernateSpringValidator;
import org.cmas.util.dao.HibernateDao;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindingResult;

public abstract class EntityServiceImpl<E>{

    protected Class<E> entityClass;

    protected HibernateSpringValidator validator;

    protected HibernateDao<E> entityDao;
     
    public void validate(E formObject, BindingResult errors) {
        validator.validate(formObject, errors);
    }

    @Required
    public void setEntityClass(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    @Required
    public void setEntityDao(HibernateDao<E> entityDao) {
        this.entityDao = entityDao;
    }

	@Required
	public void setValidator(HibernateSpringValidator validator) {
		this.validator = validator;
	}
}
