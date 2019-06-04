package org.cmas.presentation.service.loyalty;

import org.cmas.entities.loyalty.InsuranceRequest;

/**
 * Created on May 31, 2019
 *
 * @author Alexander Petukhov
 */
public interface InsuranceRequestService {

    void persistAndSendInsuranceRequest(InsuranceRequest rawRequest);
}
