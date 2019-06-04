package org.cmas.presentation.dao.loyalty;

import org.cmas.entities.diver.Diver;
import org.cmas.entities.loyalty.CameraOrder;
import org.cmas.entities.loyalty.LoyaltyProgramItem;
import org.cmas.util.dao.HibernateDao;

/**
 * Created on Feb 13, 2019
 *
 * @author Alexander Petukhov
 */
public interface CameraOrderDao extends HibernateDao<CameraOrder>{

    int getOrderCntForYear(Diver diver, LoyaltyProgramItem loyaltyProgramItem);
}
