package org.cmas.presentation.dao.user;

import org.cmas.presentation.entities.user.Registration;
import org.cmas.util.dao.HibernateDaoImpl;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SuppressWarnings({"unchecked"})
public class RegistrationDaoImpl extends HibernateDaoImpl<Registration> implements RegistrationDao {

    public RegistrationDaoImpl(Class<Registration> modelClass) {
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
    public Registration getBySec(@NotNull String sec) {
        return (Registration) createCriteria().add(Restrictions.eq("md5", sec)).uniqueResult();
    }

    @Transactional
    @Override
    public List<Registration> getReadyToRegister() {
        Criteria crit = createCriteria();
        return crit.addOrder(Order.desc("dateReg")).setCacheable(true).list();
    }
}
