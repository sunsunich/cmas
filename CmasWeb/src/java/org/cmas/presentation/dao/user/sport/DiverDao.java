package org.cmas.presentation.dao.user.sport;

import org.cmas.entities.diver.Diver;
import org.cmas.entities.sport.NationalFederation;
import org.cmas.presentation.dao.user.UserDao;
import org.cmas.presentation.model.registration.DiverVerificationFormObject;
import org.cmas.presentation.model.social.FindDiverFormObject;

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

    List<Diver> searchNotFriendDivers(long diverId, FindDiverFormObject formObject);

    List<Diver> searchFriendsFast(long diverId, String input);

    List<Diver> searchDivers(FindDiverFormObject formObject);

    List<Diver> getFriends(Diver diver);

    List<Diver> getDiversByIds(List<Long> diverIds);

    boolean isFriend(Diver diver, Diver friend);

    void removeFriend(Diver diver, Diver friend);
}
