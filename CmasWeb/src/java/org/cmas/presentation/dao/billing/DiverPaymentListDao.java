package org.cmas.presentation.dao.billing;

import org.cmas.entities.diver.Diver;
import org.cmas.presentation.entities.billing.DiverPaymentList;
import org.cmas.util.dao.IdGeneratingDao;

/**
 * Created on Mar 31, 2019
 *
 * @author Alexander Petukhov
 */
public interface DiverPaymentListDao extends IdGeneratingDao<DiverPaymentList> {

    DiverPaymentList getByCreator(Diver diver);
}
