package org.cmas.presentation.dao.loyalty;

import org.cmas.Globals;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.loyalty.CameraOrder;
import org.cmas.entities.loyalty.LoyaltyProgramItem;
import org.cmas.util.dao.HibernateDaoImpl;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.Date;

/**
 * Created on Feb 13, 2019
 *
 * @author Alexander Petukhov
 */
public class CameraOrderDaoImpl extends HibernateDaoImpl<CameraOrder> implements CameraOrderDao {

    @Override
    public int getOrderCntForYear(Diver diver, LoyaltyProgramItem loyaltyProgramItem) {
        return (Integer) createCriteria()
                .add(Restrictions.eq("diver", diver))
                .add(Restrictions.eq("loyaltyProgramItem", loyaltyProgramItem))
                .add(Restrictions.ge("createDate",
                                     new Date(System.currentTimeMillis() - Globals.getMsInYear())))
                .setProjection(Projections.rowCount()).uniqueResult();
    }
}
