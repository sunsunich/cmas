package org.cmas.presentation.dao.user;

import org.cmas.presentation.entities.user.UserClient;
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


public class UserDaoImpl extends IdGeneratingDaoImpl<UserClient> implements UserDao {

    @Transactional
    @Override
    public List<UserClient> searchUsers(UserSearchFormObject form) {
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
    public UserClient getBylostPasswdCode(@NotNull String lostPasswdCode) {
        Criteria crit = createCriteria();
        return (UserClient) crit.add(Restrictions.eq("lostPasswdCode", lostPasswdCode))
                .add(Restrictions.eq("enabled", Boolean.TRUE))
                .setCacheable(true).uniqueResult();
    }

    @Transactional
    @Override
    public UserClient getByEmail(@NotNull String email) {
        Criteria crit = createCriteria();
        return (UserClient) crit.add(Restrictions.eq("contactInfo.email", email))
                .add(Restrictions.eq("enabled", Boolean.TRUE))
                .setCacheable(true).uniqueResult();
    }

    @Override
    @Transactional
    @Nullable
    public UserClient getUserChangedEmail(@NotNull String md5) {
        return (UserClient) createCriteria().add(Restrictions.eq("md5newMail", md5)).uniqueResult();
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
        String shopName = form.getShopName();
        if (!StringUtil.isEmpty(shopName)) {
            crit.add(Restrictions.like("shopName", shopName.trim(), MatchMode.START));
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
        getSessionFactory().evict(UserClient.class, userId);
    }
}
