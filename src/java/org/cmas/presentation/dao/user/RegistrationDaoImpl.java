package org.cmas.presentation.dao.user;

import org.cmas.presentation.dao.DAOException;
import org.cmas.presentation.entities.user.Registration;
import org.cmas.util.dao.HibernateDaoImpl;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.List;

@SuppressWarnings({"unchecked"})
public class RegistrationDaoImpl extends HibernateDaoImpl<Registration> implements RegistrationDao {

    public RegistrationDaoImpl(Class<Registration> modelClass){
        super(modelClass);
    }

    @Transactional
    @Override
    public boolean isEmailUnique(String email, Long id) {
        return isCommonFieldUnique(id, "email", email);
    }

    @Transactional
    @Override
    public boolean isEmailUnique(String email) {
        return isEmailUnique(email, null);
    }

    @Override
    @Transactional
    public Registration getByIdAndSec(@NotNull Long id, @NotNull String sec) {
        Criteria crit = createCriteria();
        return (Registration) crit.add(Restrictions.eq("id", id))
                .add(Restrictions.eq("md5", sec)).uniqueResult();
    }

    @Transactional
    @Override
    public List<Registration> getReadyToRegister() {
        Criteria crit = createCriteria();
        return crit
                //  .add(Restrictions.eq("confirm", true)) хз что такое поле confirm. 
                .addOrder(Order.desc("dateReg")).setCacheable(true).list();
    }

    @Override
    public Registration createNew() throws DAOException {
        Class<Registration> cl = getModelClass();
        try {
            Constructor<Registration> constructor = cl.getConstructor(Date.class);
            return constructor.newInstance(new Date());
        } catch (Exception e) {
            throw new DAOException("Error initiating object of class: "+cl.getName(),e);
        }

    }

}
