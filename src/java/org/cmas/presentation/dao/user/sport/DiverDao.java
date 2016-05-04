package org.cmas.presentation.dao.user.sport;

import org.cmas.entities.diver.Diver;
import org.cmas.entities.sport.NationalFederation;
import org.cmas.presentation.dao.user.UserDao;
import org.cmas.presentation.model.registration.DiverVerificationFormObject;

import java.util.Date;
import java.util.List;

/**
 * Created on Feb 05, 2016
 *
 * @author Alexander Petukhov
 */
public interface DiverDao extends UserDao<Diver> {

    Diver searchDiver(NationalFederation federation, String firstName, String lastName, Date dob);

    int getFullyRegisteredDiverCnt();

    Diver getDiverByCardNumber(String cardNumber);

    List<Diver> searchForVerification(DiverVerificationFormObject formObject);
}
