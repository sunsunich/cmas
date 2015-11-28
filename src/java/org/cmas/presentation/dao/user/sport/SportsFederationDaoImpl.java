package org.cmas.presentation.dao.user.sport;

import org.cmas.entities.Country;
import org.cmas.entities.sport.SportsFederation;
import org.cmas.presentation.dao.DictionaryDataDaoImpl;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

/**
 * Created on Nov 24, 2015
 *
 * @author Alexander Petukhov
 */
public class SportsFederationDaoImpl extends DictionaryDataDaoImpl<SportsFederation> implements SportsFederationDao{

    @Override
    public SportsFederation getFederationForSportsman(String firstName, String lastName, Country country) {
        String hql = "select sf from org.cmas.presentation.dao.user.sport.SportsFederation" +
                     " inner join sf.sportsmanList sp" +
                     " where sp.firstName = :firstName and sp.lastName = :lastName and sp.country.id = :countryId"
                ;
        Query query = createQuery(hql);
        query.setString("firstName", firstName);
        query.setString("lastName", lastName);
        query.setLong("countryId", country.getId());
        return (SportsFederation)query.uniqueResult();
    }

    @Override
    public SportsFederation getByCountry(Country country) {
        return (SportsFederation)createCriteria().add(Restrictions.eq("country", country)).uniqueResult();
    }
}
