package org.cmas.presentation.dao.user.sport;

import org.cmas.entities.sport.SportsmanCardType;
import org.cmas.util.dao.HibernateDaoImpl;
import org.hibernate.criterion.Restrictions;

/**
 * Created on Nov 22, 2015
 *
 * @author Alexander Petukhov
 */
public class SportsmanCardTypeDaoImpl extends HibernateDaoImpl<SportsmanCardType> implements SportsmanCardTypeDao {

    @Override
    public SportsmanCardType getPrimaryCardType() {
        return (SportsmanCardType)createCriteria().add(Restrictions.eq("name", "primary")).uniqueResult();
    }
}
