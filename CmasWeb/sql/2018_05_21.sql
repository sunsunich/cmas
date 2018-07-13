update divers set diverRegistrationStatus = "NEVER_REGISTERED" where password is null and role = 'ROLE_DIVER' and enabled = true;
update divers set diverRegistrationStatus = "CMAS_BASIC" where password is not null and hasPayed = false and role = 'ROLE_DIVER' and enabled = true;
update divers set diverRegistrationStatus = "CMAS_FULL" where hasPayed = true and role = 'ROLE_DIVER' and enabled = true;
update divers set diverRegistrationStatus = "INACTIVE" where enabled = false;
update divers set dateLicencePaymentIsDue = '2019-05-21 20:45:49' where diverRegistrationStatus = 'CMAS_FULL';
