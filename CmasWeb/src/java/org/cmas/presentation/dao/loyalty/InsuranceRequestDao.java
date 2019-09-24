package org.cmas.presentation.dao.loyalty;

import org.cmas.entities.loyalty.InsuranceRequest;
import org.cmas.util.dao.HibernateDao;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created on May 31, 2019
 *
 * @author Alexander Petukhov
 */
public interface InsuranceRequestDao extends HibernateDao<InsuranceRequest> {

    List<InsuranceRequest> getAllUnsent();

    @Nullable
    InsuranceRequest getLatestPaidInsuranceRequestForDiver(long diverId);
}
