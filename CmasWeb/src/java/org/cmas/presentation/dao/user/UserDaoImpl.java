package org.cmas.presentation.dao.user;

import org.cmas.entities.Role;
import org.cmas.entities.User;
import org.cmas.presentation.model.user.UserSearchFormObject;
import org.cmas.util.StringUtil;
import org.cmas.util.dao.IdGeneratingDaoImpl;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
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
        return isCommonFieldUnique(id, "email", email);
    }

    @Transactional
    @Override
    public boolean isEmailUnique(String email) {
        return isEmailUnique(email, null);
    }

    @Override
    public Serializable save(@NotNull T model) {
        model.setLastAction(new Date());
        return super.save(model);
    }

    @Override
    public void saveModel(@NotNull T model) {
        model.setLastAction(new Date());
        super.saveModel(model);
    }

    @Override
    public void updateModel(@NotNull T model) {
        model.setLastAction(new Date());
        super.updateModel(model);
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
        return (T) crit.add(Restrictions.eq("email", email))
                       .add(Restrictions.eq("enabled", Boolean.TRUE))
                       .setCacheable(true).uniqueResult();
    }

    @Override
    public T getByFirstNameLastNameCountry(@NotNull String firstName, @NotNull String lastName, @NotNull String countryCode) {
        Criteria crit = createCriteria();
        List divers = crit
                .createAlias("country", "country")
                .add(Restrictions.eq("country.code", StringUtil.correctSpaceCharAndTrim(countryCode)))
                .add(Restrictions.eq("firstName", firstName))
                .add(Restrictions.eq("lastName", lastName))
                .add(Restrictions.eq("enabled", Boolean.TRUE))
                .setCacheable(true).list();
        //noinspection unchecked
        return (T) (divers.isEmpty() ? null : divers.get(0));
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
        Object result = crit.uniqueResult();
        return result == null ? 0 : ((Number) result).intValue();
    }

    protected Criteria makeSearchRequest(UserSearchFormObject form) {
        Criteria crit = createCriteria().add(Restrictions.eq("enabled", true));
        String email = form.getEmail();
        if (!StringUtil.isTrimmedEmpty(email)) {
            crit.add(Restrictions.like("email", StringUtil.correctSpaceCharAndTrim(email),
                                       MatchMode.START));
        }
        String firstName = form.getFirstName();
        if (!StringUtil.isTrimmedEmpty(firstName)) {
            crit.add(Restrictions.like("firstName", StringUtil.correctSpaceCharAndTrim(firstName),
                                       MatchMode.START));
        }
        String lastName = form.getLastName();
        if (!StringUtil.isTrimmedEmpty(lastName)) {
            crit.add(Restrictions.like("lastName", StringUtil.correctSpaceCharAndTrim(lastName), MatchMode.START));
        }
        String userRole = form.getUserRole();
        if (!StringUtil.isTrimmedEmpty(userRole)) {
            crit.add(Restrictions.eq("role", Role.valueOf(userRole)));
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
