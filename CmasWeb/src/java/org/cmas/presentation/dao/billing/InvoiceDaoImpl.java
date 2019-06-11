package org.cmas.presentation.dao.billing;

import org.cmas.entities.billing.Invoice;
import org.cmas.presentation.entities.user.BackendUser;
import org.cmas.util.dao.IdGeneratingDaoImpl;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.List;


public class InvoiceDaoImpl extends IdGeneratingDaoImpl<Invoice> implements InvoiceDao{

	@Override
	public List<Invoice> getAllNotRemoved() {
        //noinspection unchecked
		return createNotRemovedOrdered().list();
	}

	@Override
	public List<Invoice> getAllNotRemovedByUser(BackendUser u) {
        //noinspection unchecked
        return createNotRemovedOrdered().add(Restrictions.eq("user",u)).list();
	}

    @Override
    public Invoice getByExternalInvoiceNumber(String externalInvoiceNumber) {
        return (Invoice)createNotRemovedOrdered().add(
                Restrictions.eq("externalInvoiceNumber", externalInvoiceNumber)
        ).uniqueResult();
    }

    private Criteria createNotRemovedOrdered() {
		return createCriteria().add(Restrictions.eq("deleted", false)).addOrder(Order.desc("createDate"));
	}
}
