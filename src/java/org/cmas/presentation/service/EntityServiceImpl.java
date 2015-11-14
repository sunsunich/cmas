package org.cmas.presentation.service;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindingResult;
import org.cmas.presentation.model.Editable;
import org.cmas.presentation.model.TransferException;
import org.cmas.presentation.model.Transferable;
import org.cmas.presentation.validator.HibernateSpringValidator;
import org.cmas.util.dao.HibernateDao;

public abstract class EntityServiceImpl<F extends Transferable<E>,E>{

    protected Class<F> formObjectClass;

    protected Class<E> entityClass;

    protected HibernateSpringValidator validator;

    protected HibernateDao<E> entityDao;
     
    public void validate(F formObject, BindingResult errors) {
        validator.validate(formObject, errors);
    }

    public F entityToFormObject(E entity){
        try {
            F formObject = formObjectClass.newInstance();
            formObject.transferFromEntity(entity);
            return formObject;
        } catch (InstantiationException e) {
            throw new TransferException("formObject must allways have default constructor",e);
        } catch (IllegalAccessException e) {
            throw new TransferException("formObject must allways have default constructor",e);
        }      
    }

    public E formObjectToEntity(F formObject){    
        Long id = null;
        if(formObject instanceof Editable){
            //noinspection RawUseOfParameterizedType
            id = ((Editable)formObject).getId();
        }
        E entity;
        if(id == null){
            entity = createNew();
        }
        else{
            entity = entityDao.getById(id);
        }
        formObject.transferToEntity(entity);
        return entity;
    }

    public HibernateDao<E> getEntityDao() {
        return entityDao;
    }

    protected abstract E createNew();

    @Required
    public void setFormObjectClass(Class<F> formObjectClass) {
        this.formObjectClass = formObjectClass;
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
