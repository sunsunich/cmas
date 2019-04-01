package org.cmas.presentation.dao.billing;

import org.cmas.entities.diver.Diver;
import org.cmas.presentation.entities.billing.DiverPaymentList;
import org.cmas.util.dao.IdGeneratingDaoImpl;
import org.hibernate.criterion.Restrictions;

/**
 * Created on Mar 31, 2019
 *
 * @author Alexander Petukhov
 */
public class DiverPaymentListDaoImpl extends IdGeneratingDaoImpl<DiverPaymentList> implements DiverPaymentListDao {

    @Override
    public DiverPaymentList getByCreator(Diver diver) {
        return (DiverPaymentList) createCriteria()
                .add(Restrictions.eq("listCreator", diver))
                .uniqueResult();
    }
}
