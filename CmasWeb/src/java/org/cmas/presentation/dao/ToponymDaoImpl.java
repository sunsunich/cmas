package org.cmas.presentation.dao;

import org.cmas.entities.Country;
import org.cmas.entities.Toponym;
import org.hibernate.criterion.Restrictions;

/**
 * Created on Nov 22, 2015
 *
 * @author Alexander Petukhov
 */
public class ToponymDaoImpl extends DictionaryDataDaoImpl<Toponym> implements ToponymDao {

    @Override
    public Toponym getByCountryAndName(String name, Country country) {
        return (Toponym) createNotDeletedCriteria()
                .createAlias("countries", "country")
                .add(Restrictions.eq("name", name))
                .add(Restrictions.eq("country.id", country.getId()))
                .uniqueResult();
    }
}
