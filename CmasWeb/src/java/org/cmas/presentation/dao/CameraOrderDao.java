package org.cmas.presentation.dao;

import org.cmas.entities.diver.Diver;
import org.cmas.entities.fin.LoyaltyProgramItem;
import org.cmas.presentation.entities.CameraOrder;
import org.cmas.util.dao.HibernateDao;

/**
 * Created on Feb 13, 2019
 *
 * @author Alexander Petukhov
 */
public interface CameraOrderDao extends HibernateDao<CameraOrder>{

    int getOrderCntForYear(Diver diver, LoyaltyProgramItem loyaltyProgramItem);
}
