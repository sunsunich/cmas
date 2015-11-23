package org.cmas.presentation.dao.user;

import org.cmas.entities.User;
import org.cmas.presentation.model.user.UserSearchFormObject;
import org.cmas.util.dao.IdGeneratingDaoImpl;
import org.cmas.util.text.StringUtil;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public class UserDaoImpl<T extends User> extends IdGeneratingDaoImpl<T> implements UserDao<T> {

    @Transactional
    @Override
    public List<T> searchUsers(UserSearchFormObject form) {
        Criteria crit = makeSearchRequest(form);
        boolean dir = form.isDir();
        String sort = form.getSortColumnName();
        if (sort == null) {
            sort = UserSearchFormObject.UserReportColumnNames.email.toString();
        }
        Order order = getOrder(sort, dir);
        //noinspection unchecked
        return crit.addOrder(order).setFirstResult(form.getOffset())
                .setMaxResults(form.getLimit()).setCacheable(true).list();
    }
	                          
    @Transactional
    @Override
    public boolean isEmailUnique(String email, Long id) {
        return isCommonFieldUnique(id, "contactInfo.email", email);
    }

    @Transactional
    @Override
    public boolean isEmailUnique(String email) {
        return isEmailUnique(email, null);
    }

    @Transactional
    @Override
    public T getBylostPasswdCode(@NotNull String lostPasswdCode) {
        Criteria crit = createCriteria();
        //noinspection unchecked
        return (T) crit.add(Restrictions.eq("lostPasswdCode", lostPasswdCode))
                .add(Restrictions.eq("enabled", Boolean.TRUE))
                .setCacheable(true).uniqueResult();
    }

    @Transactional
    @Override
    public T getByEmail(@NotNull String email) {
        Criteria crit = createCriteria();
        //noinspection unchecked
        return (T) crit.add(Restrictions.eq("contactInfo.email", email))
                .add(Restrictions.eq("enabled", Boolean.TRUE))
                .setCacheable(true).uniqueResult();
    }

    @Override
    @Transactional
    @Nullable
    public T getUserChangedEmail(@NotNull String md5) {
        //noinspection unchecked
        return (T) createCriteria().add(Restrictions.eq("md5newMail", md5)).uniqueResult();
    }

    @Transactional
    @Override
    public int getMaxCountSearchUsers(UserSearchFormObject form) {
        Criteria crit = makeSearchRequest(form);
        crit.setProjection(Projections.rowCount()).setCacheable(true);
        return (Integer) crit.uniqueResult();
    }

    private Criteria makeSearchRequest(UserSearchFormObject form) {
        Criteria crit = createCriteria().add(Restrictions.eq("enabled", true)); 
        String email = form.getEmail();
        if (!StringUtil.isEmpty(email)) {
            crit.add(Restrictions.like("contactInfo.email", email.trim(), MatchMode.START));
        }
        return crit;
    }

    private Order getOrder(String name, boolean dir) {
        if (dir) {
            return Order.desc(name);
        }
        return Order.asc(name);
    }

    @Override
    public void evictUser(long userId) {
        getSessionFactory().evict(getModelClass(), userId);
    }
}
