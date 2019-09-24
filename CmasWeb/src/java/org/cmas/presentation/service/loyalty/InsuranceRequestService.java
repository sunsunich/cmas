package org.cmas.presentation.service.loyalty;

import org.cmas.entities.diver.Diver;
import org.cmas.entities.loyalty.InsuranceRequest;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

/**
 * Created on May 31, 2019
 *
 * @author Alexander Petukhov
 */
public interface InsuranceRequestService {

    @Nullable
    Date getDiverInsuranceExpiryDate(Diver diver);

    void persistAndSendInsuranceRequest(InsuranceRequest rawRequest);
}
