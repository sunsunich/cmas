package org.cmas.presentation.dao.user.sport;

import org.cmas.entities.sport.SportsmanCardType;
import org.cmas.util.dao.HibernateDao;

/**
 * Created on Nov 22, 2015
 *
 * @author Alexander Petukhov
 */
public interface SportsmanCardTypeDao extends HibernateDao<SportsmanCardType> {

    SportsmanCardType getPrimaryCardType();
}
