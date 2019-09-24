package org.cmas.presentation.dao.loyalty;

import org.cmas.entities.loyalty.InsuranceRequest;
import org.cmas.util.dao.HibernateDaoImpl;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created on May 31, 2019
 *
 * @author Alexander Petukhov
 */
@SuppressWarnings("unchecked")
public class InsuranceRequestDaoImpl extends HibernateDaoImpl<InsuranceRequest> implements InsuranceRequestDao {

    @Override
    public List<InsuranceRequest> getAllUnsent() {
        return (List<InsuranceRequest>) createCriteria().add(Restrictions.eq("sent", false)).list();
    }

    @Nullable
    @Override
    public InsuranceRequest getLatestPaidInsuranceRequestForDiver(long diverId) {
        List<InsuranceRequest> requests = createCriteria()
                .add(Restrictions.eq("diver.id", diverId))
                .addOrder(Order.desc("createDate"))
                .list();
        return requests.isEmpty() ? null : requests.get(0);
    }
}
