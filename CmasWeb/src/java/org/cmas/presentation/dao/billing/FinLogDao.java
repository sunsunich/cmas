package org.cmas.presentation.dao.billing;

import org.cmas.presentation.entities.billing.FinLog;
import org.cmas.presentation.entities.user.BackendUser;
import org.cmas.presentation.model.user.fin.FinStatsFormObject;
import org.cmas.util.dao.HibernateDao;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.List;

public interface FinLogDao extends HibernateDao<FinLog> {

    List<FinLog> getByUser(BackendUser u, FinStatsFormObject formObject);

    int countForUser(BackendUser u, FinStatsFormObject formObject);

    @Nullable
    BigDecimal getAllUserInMoney(BackendUser u);
}
