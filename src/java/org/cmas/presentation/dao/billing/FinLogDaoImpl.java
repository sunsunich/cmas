package org.cmas.presentation.dao.billing;

import org.cmas.presentation.entities.billing.FinLog;
import org.cmas.presentation.entities.billing.OperationType;
import org.cmas.presentation.entities.user.UserClient;
import org.cmas.presentation.model.user.fin.FinStatsFormObject;
import org.cmas.Globals;
import org.cmas.util.dao.SearchDaoImpl;
import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.List;

public class FinLogDaoImpl  extends SearchDaoImpl<FinLog> implements FinLogDao {//, InitializingBean{

    @Override
    public List<FinLog> getByUser(UserClient u, FinStatsFormObject formObject) {
        Criteria criteria = createSearchCriteria(u, formObject);
        return search(criteria, formObject);
    }

    private Criteria createSearchCriteria(UserClient u, FinStatsFormObject formObject) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("user",u));
        List<OperationType> operationTypes = formObject.getOperationTypes();
        if (!operationTypes.isEmpty()) {
            Disjunction disjunction = Restrictions.disjunction();
            criteria.add(disjunction);
            for (OperationType operationType : operationTypes) {
                disjunction.add(Restrictions.eq("operationType", operationType));
            }
        }
        addDateRangeToSearchCriteria(
                  criteria, formObject.getFromDate(),  formObject.getToDate()
                , "recordDate"
                , Globals.getDTF()
                );
        return criteria;
    }

    @Override
    public int countForUser(UserClient u, FinStatsFormObject formObject) {
        return getMaxCountSearch(createSearchCriteria(u, formObject));
    }

    @Override
    @Nullable
    public BigDecimal getAllUserInMoney(UserClient u) {
        Criteria criteria = createCriteria();
		criteria.add(Restrictions.eq("user",u))
                .add(Restrictions.eq("operationType", OperationType.IN))
          //      .add(Restrictions.eq("coinType", CoinType.UNI_COINS))
                ;
        criteria.setProjection(Projections.sum("coins"));
        return (BigDecimal)criteria.uniqueResult();
    }   
}
