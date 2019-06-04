package org.cmas.presentation.service.loyalty;

import org.cmas.entities.loyalty.InsuranceRequest;
import org.cmas.presentation.service.loyalty.bf.BalticFinanceResponse;

/**
 * Created on May 31, 2019
 *
 * @author Alexander Petukhov
 */
public interface InsuranceCompanyRemoteService {

    BalticFinanceResponse sendInsuranceRequest(InsuranceRequest request) throws Exception;
}
