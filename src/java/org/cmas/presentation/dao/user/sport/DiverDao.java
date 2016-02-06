package org.cmas.presentation.dao.user.sport;

import org.cmas.entities.diver.Diver;
import org.cmas.entities.sport.SportsFederation;
import org.cmas.presentation.dao.user.UserDao;

import java.util.Date;

/**
 * Created on Feb 05, 2016
 *
 * @author Alexander Petukhov
 */
public interface DiverDao extends UserDao<Diver> {

    Diver searchDiver(SportsFederation federation, String firstName, String lastName, Date dob);
}
