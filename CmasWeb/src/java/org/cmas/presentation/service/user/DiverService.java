package org.cmas.presentation.service.user;

import org.cmas.entities.billing.Invoice;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.sport.NationalFederation;
import org.cmas.presentation.model.user.DiverFormObject;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * Created on Apr 29, 2016
 *
 * @author Alexander Petukhov
 */
public interface DiverService extends UserService<Diver> {

    String scheduleUploadDivers(Diver fedAdmin, MultipartFile file) throws IOException;

    UploadDiversTask getUploadDiversTask(long fedAdminId);

    void uploadSingleDiver(NationalFederation federation, Diver diver);

    void updateDiverTypeAndLevelBasingOnCards(@Nonnull Diver dbDiver, boolean forceUpdate);

    void addGuestDiverToFederation(NationalFederation federation, Diver dbDiver);

    void diverPaidForFeature(Diver diver, Invoice invoice, boolean isConfirmEmail);

    String getDiverStatusString(Diver diver);

    int getDemoTimeDays();

    void editDiver(DiverFormObject formObject, Diver diver);
}
