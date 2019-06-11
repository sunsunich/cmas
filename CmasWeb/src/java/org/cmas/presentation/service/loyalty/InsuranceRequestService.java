package org.cmas.presentation.service.loyalty;

import org.cmas.entities.billing.Invoice;
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

    void createInsuranceRequest(InsuranceRequest rawRequest);

    @Nullable
    Date getDiverInsuranceExpiryDate(Diver diver);

    boolean canCreateInvoiceWithInsuranceRequest(Diver diver);

    // used by admin only
    void persistAndSendInsuranceRequest(InsuranceRequest rawRequest);

    void sendInsuranceRequest(Invoice invoice);
}
