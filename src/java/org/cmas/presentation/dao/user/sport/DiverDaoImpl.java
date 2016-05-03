package org.cmas.presentation.dao.user.sport;

import org.cmas.entities.diver.Diver;
import org.cmas.entities.sport.NationalFederation;
import org.cmas.presentation.dao.user.UserDaoImpl;
import org.cmas.presentation.model.registration.DiverVerificationFormObject;
import org.cmas.presentation.model.user.UserSearchFormObject;
import org.cmas.util.text.StringUtil;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.Date;
import java.util.List;

/**
 * Created on Feb 05, 2016
 *
 * @author Alexander Petukhov
 */
@SuppressWarnings("unchecked")
public class DiverDaoImpl extends UserDaoImpl<Diver> implements DiverDao {

    @Override
    public Diver searchDiver(NationalFederation federation, String firstName, String lastName, Date dob) {
        return (Diver) createCriteria()
                .add(Restrictions.eq("dob", dob))
                .add(Restrictions.eq("firstName", firstName))
                .add(Restrictions.eq("lastName", lastName))
                .add(Restrictions.eq("federation", federation))
                .uniqueResult();
    }

    @Override
    protected Criteria makeSearchRequest(UserSearchFormObject form) {
        Criteria criteria = super.makeSearchRequest(form);
        String country = form.getCountryCode();
        if (!StringUtil.isTrimmedEmpty(country)) {
            criteria.createAlias("federation", "fed")
                    .createAlias("fed.country", "country")
                    .add(Restrictions.eq("country.code", country.trim()));
        }
        return criteria;
    }

    @Override
    public int getFullyRegisteredDiverCnt() {
        return (Integer) createCriteria().add(Restrictions.isNotNull("primaryPersonalCard"))
                                         .setProjection(Projections.count("id"))
                                         .uniqueResult();
    }

    @Override
    public Diver getDiverBySecondaryCardNumber(String cardNumber) {
        String hql = "select d from org.cmas.entities.diver.Diver d"
                     + " inner join d.secondaryPersonalCards c where c.number = :number";
        return (Diver) createQuery(hql).setString("number", cardNumber).uniqueResult();
    }

    @Override
    public List<Diver> searchForVerification(DiverVerificationFormObject formObject) {
        return (List<Diver>) createCriteria()
                .add(Restrictions.eq("lastName", formObject.getLastName()))
                .createAlias("federation", "fed")
                .createAlias("fed.country", "country")
                .add(Restrictions.eq("country.code", formObject.getCountry().trim()))
                .list();
    }
}
