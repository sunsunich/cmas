package org.cmas.presentation.service.user;

import org.cmas.entities.PersonalCard;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.sport.NationalFederation;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Created on Apr 29, 2016
 *
 * @author Alexander Petukhov
 */
public interface DiverService extends UserService<Diver> {

    List<PersonalCard> getCardsToShow(Diver diver);

    String scheduleUploadDivers(Diver fedAdmin, MultipartFile file) throws IOException;

    UploadDiversTask getUploadDiversTask(long fedAdminId);

    void uploadDiver(NationalFederation federation, Diver diver, boolean overrideCards);
}
