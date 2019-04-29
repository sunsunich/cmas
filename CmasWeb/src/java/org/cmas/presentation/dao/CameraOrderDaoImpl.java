package org.cmas.presentation.dao;

import org.cmas.Globals;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.fin.LoyaltyProgramItem;
import org.cmas.presentation.entities.CameraOrder;
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
                                     new Date(diver.getDateLicencePaymentIsDue().getTime() - Globals.getMsInYear())))
                .setProjection(Projections.rowCount()).uniqueResult();
    }
}
