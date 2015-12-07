package org.cmas.presentation.dao.user;

import org.cmas.entities.PersonalCardType;
import org.cmas.presentation.dao.DictionaryDataDaoImpl;
import org.hibernate.criterion.Restrictions;

/**
 * Created on Nov 22, 2015
 *
 * @author Alexander Petukhov
 */
public class PersonalCardTypeDaoImpl extends DictionaryDataDaoImpl<PersonalCardType> implements PersonalCardTypeDao {

    @Override
    public PersonalCardType getPrimaryCardType() {
        return (PersonalCardType)createCriteria().add(Restrictions.eq("name", "primary")).uniqueResult();
    }
}
