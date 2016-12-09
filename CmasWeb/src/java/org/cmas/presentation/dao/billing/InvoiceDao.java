package org.cmas.presentation.dao.billing;

import org.cmas.presentation.entities.billing.Invoice;
import org.cmas.presentation.entities.user.BackendUser;
import org.cmas.util.dao.IdGeneratingDao;

import java.util.List;

public interface InvoiceDao extends IdGeneratingDao<Invoice> {

	List<Invoice> getAllNotRemoved();

	List<Invoice> getAllNotRemovedByUser(BackendUser u);

    Invoice getByExternalInvoiceNumber(String externalInvoiceNumber);
}
