package org.cmas.presentation.dao.elearning;

import org.cmas.entities.diver.Diver;
import org.cmas.entities.elearning.ElearningToken;
import org.cmas.util.dao.HibernateDaoImpl;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import javax.annotation.Nullable;
import java.util.List;

public class ElearningTokenDaoImpl extends HibernateDaoImpl<ElearningToken> implements ElearningTokenDao {

    @Nullable
    @Override
    public ElearningToken getByDiver(Diver diver) {
        Object result = createCriteria().add(Restrictions.eq("diver", diver)).uniqueResult();
        return result == null ? null : (ElearningToken) result;
    }

    @Nullable
    @Override
    public ElearningToken getNextNewToken() {
        @SuppressWarnings("unchecked")
        List<ElearningToken> tokens = createCriteria().add(Restrictions.isNull("diver")).list();
        if (tokens.isEmpty()) {
            return null;
        }
        return tokens.get(0);
    }

    @Override
    public int countAvailableTokens() {
        Object result = createCriteria().add(Restrictions.isNull("diver"))
                                        .setProjection(Projections.rowCount())
                                        .uniqueResult();
        return result == null ? 0 : ((Number) result).intValue();
    }
}
