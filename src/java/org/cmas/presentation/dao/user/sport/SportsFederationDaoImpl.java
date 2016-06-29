package org.cmas.presentation.dao.user.sport;

import org.cmas.entities.Country;
import org.cmas.entities.sport.NationalFederation;
import org.cmas.presentation.dao.DictionaryDataDaoImpl;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

/**
 * Created on Nov 24, 2015
 *
 * @author Alexander Petukhov
 */
public class SportsFederationDaoImpl extends DictionaryDataDaoImpl<NationalFederation> implements SportsFederationDao{

    //todo remove?
    @Override
    public NationalFederation getFederationForSportsman(String firstName, String lastName, Country country) {
        String hql = "select sf from org.cmas.presentation.dao.user.sport.NationalFederation" +
                     " inner join sf.athleteList a" +
                     " where a.firstName = :firstName and a.lastName = :lastName and a.country.id = :countryId"
                ;
        Query query = createQuery(hql);
        query.setString("firstName", firstName);
        query.setString("lastName", lastName);
        query.setLong("countryId", country.getId());
        return (NationalFederation)query.uniqueResult();
    }

    @Override
    public NationalFederation getByCountry(Country country) {
        return (NationalFederation)createNotDeletedCriteria().add(Restrictions.eq("country", country)).uniqueResult();
    }
}
