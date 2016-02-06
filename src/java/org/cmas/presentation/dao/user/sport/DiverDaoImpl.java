package org.cmas.presentation.dao.user.sport;

import org.cmas.entities.diver.Diver;
import org.cmas.entities.sport.SportsFederation;
import org.cmas.presentation.dao.user.UserDaoImpl;
import org.hibernate.criterion.Restrictions;

import java.util.Date;

/**
 * Created on Feb 05, 2016
 *
 * @author Alexander Petukhov
 */
public class DiverDaoImpl extends UserDaoImpl<Diver> implements DiverDao{

    @Override
    public Diver searchDiver(SportsFederation federation, String firstName, String lastName, Date dob) {
        return (Diver)createCriteria()
                .add(Restrictions.eq("dob", dob))
                .add(Restrictions.eq("firstName", firstName))
                .add(Restrictions.eq("lastName", lastName))
                .add(Restrictions.eq("federation", federation))
                .uniqueResult();
    }
}
