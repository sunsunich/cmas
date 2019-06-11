package org.cmas.presentation.dao.loyalty;

import org.cmas.entities.loyalty.InsuranceRequest;
import org.cmas.util.dao.HibernateDaoImpl;
import org.hibernate.criterion.Restrictions;

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

    @Override
    public InsuranceRequest getDraftByDiver(long diverId) {
        return (InsuranceRequest) createCriteria()
                .add(Restrictions.isNull("invoice"))
                .add(Restrictions.eq("diver.id", diverId))
                .uniqueResult();
    }

    @Override
    public InsuranceRequest getLatestPaidInsuranceRequestForDiver(long diverId) {
        String hql = "select ir from org.cmas.entities.loyalty.InsuranceRequest ir"
                     + " where ir.createDate = (select max(irr.createDate)"
                     + " from org.cmas.entities.loyalty.InsuranceRequest irr"
                     + " inner join irr.diver d"
                     + " where irr.invoice != null and d.id = :diverId)";
        List<InsuranceRequest> requests = createQuery(hql).setLong("diverId", diverId).list();
        return requests.isEmpty() ? null : requests.get(0);
    }
}
