package org.cmas.presentation.dao.user.sport;

import org.cmas.entities.diver.Diver;
import org.cmas.entities.diver.DiverRegistrationStatus;
import org.cmas.entities.diver.DiverType;
import org.cmas.entities.sport.NationalFederation;
import org.cmas.presentation.dao.user.UserDao;
import org.cmas.presentation.model.registration.DiverVerificationFormObject;
import org.cmas.presentation.model.social.FindDiverFormObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.List;

/**
 * Created on Feb 05, 2016
 *
 * @author Alexander Petukhov
 */
public interface DiverDao extends UserDao<Diver> {

    List<Diver> searchDivers(NationalFederation federation, String firstName, String lastName, Date dob,
                             DiverRegistrationStatus registrationStatus);

    List<Diver> getDiversByCardNumber(String cardNumber, DiverRegistrationStatus registrationStatus);

    Diver getDiverByCardNumber(NationalFederation federation, String cardNumber);

    List<Diver> searchForVerification(DiverVerificationFormObject formObject);

    List<Diver> searchNotFriendDivers(long diverId, FindDiverFormObject formObject);

    List<Diver> searchDiversFast(long diverId, String input, DiverType diverType);

    List<Diver> searchFriendsFast(long diverId, String input);

    List<Diver> searchInFriendsFast(long diverId, String input);

    List<Diver> searchDivers(FindDiverFormObject formObject);

    List<Diver> getFriends(Diver diver);

    List<Diver> getDiversByIds(List<Long> diverIds);

    boolean isFriend(Diver diver, Diver friend);

    void removeFriend(Diver diver, Diver friend);

    void updateDiverRegistrationStatusOnPaymentDueDate();

    @Nullable
    Diver getByFirstNameLastNameCountry(@NotNull String firstName, @NotNull String lastName, @NotNull String countryCode);
}
