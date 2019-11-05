update divers set dateLicencePaymentIsDue = DATE_ADD(NOW(), INTERVAL +30 DAY)
where diverRegistrationStatus = 'CMAS_BASIC' and dateLicencePaymentIsDue is null;