package org.cmas.presentation.dao.user.sport;

import org.cmas.entities.sport.SportsmanCardType;
import org.cmas.presentation.dao.DictionaryDataDao;

/**
 * Created on Nov 22, 2015
 *
 * @author Alexander Petukhov
 */
public interface SportsmanCardTypeDao extends DictionaryDataDao<SportsmanCardType> {

    SportsmanCardType getPrimaryCardType();
}
