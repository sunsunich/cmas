package org.cmas.presentation.dao.user;

import org.cmas.entities.PersonalCardType;
import org.cmas.presentation.dao.DictionaryDataDao;

/**
 * Created on Nov 22, 2015
 *
 * @author Alexander Petukhov
 */
public interface PersonalCardTypeDao extends DictionaryDataDao<PersonalCardType> {

    PersonalCardType getPrimaryCardType();
}
