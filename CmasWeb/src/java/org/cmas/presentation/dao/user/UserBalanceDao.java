package org.cmas.presentation.dao.user;

import org.cmas.entities.UserBalance;
import org.cmas.util.dao.HibernateDao;

import java.math.BigDecimal;

public interface UserBalanceDao extends HibernateDao<UserBalance>{


    void addToBalance(long userId, BigDecimal money);

    void withdrawFromBalance(long userId, BigDecimal money);
}
