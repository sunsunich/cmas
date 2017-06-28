package org.cmas.presentation.service.user;

import org.cmas.entities.PersonalCard;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.sport.NationalFederation;

import java.io.InputStream;
import java.util.List;

/**
 * Created on Apr 29, 2016
 *
 * @author Alexander Petukhov
 */
public interface DiverService extends UserService<Diver> {

    void uploadDivers(NationalFederation federation, InputStream file, ProgressListener progressListener) throws Exception;

    List<PersonalCard> getCardsToShow(Diver diver);

    @SuppressWarnings({"CallToStringEqualsIgnoreCase", "CallToStringEquals", "ObjectAllocationInLoop"})
    void uploadDiver(NationalFederation federation, Diver diver, boolean overrideCards);
}
