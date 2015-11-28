package org.cmas.presentation.dao.user;

import org.cmas.entities.UserBalance;
import org.cmas.util.dao.HibernateDaoImpl;

import java.math.BigDecimal;

public class UserBalanceDaoImpl extends HibernateDaoImpl<UserBalance> implements UserBalanceDao {

    @Override
    public void addToBalance(long userId, BigDecimal money) {
        createSQLQuery(
                "update user_balances set balance = balance + :money where id = :userId"
        ).setBigDecimal("money", money)
         .setLong("userId", userId)
         .executeUpdate();
    }

    @Override
    public void withdrawFromBalance(long userId, BigDecimal money) {
        createSQLQuery(
                "update user_balances set balance = balance - :money where id = :userId and "
        ).setBigDecimal("money", money)
         .setLong("userId", userId)
         .executeUpdate();
    }
}
