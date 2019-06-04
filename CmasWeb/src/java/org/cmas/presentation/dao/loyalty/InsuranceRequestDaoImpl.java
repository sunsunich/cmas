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
public class InsuranceRequestDaoImpl extends HibernateDaoImpl<InsuranceRequest> implements InsuranceRequestDao {
    @Override
    public List<InsuranceRequest> getAllUnsent() {
        return (List<InsuranceRequest>) createCriteria().add(Restrictions.eq("sent", false)).list();
    }
}
