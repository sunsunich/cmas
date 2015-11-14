package org.cmas.presentation.dao.billing;

import org.cmas.presentation.entities.billing.Invoice;
import org.cmas.presentation.entities.user.UserClient;
import org.cmas.util.dao.IdGeneratingDao;

import java.util.List;

public interface InvoiceDao extends IdGeneratingDao<Invoice> {

	List<Invoice> getAllNotRemoved();

	List<Invoice> getAllNotRemovedByUser(UserClient u);

    Invoice getByExternalInvoiceNumber(String externalInvoiceNumber);
}
