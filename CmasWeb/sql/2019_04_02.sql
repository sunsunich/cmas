update divers set diverRegistrationStatus = 'NEVER_REGISTERED' where password is null and role = 'ROLE_DIVER' and enabled = true;
update divers set previousRegistrationStatus = 'NEVER_REGISTERED' where diverRegistrationStatus = 'NEVER_REGISTERED';
delete from diver_new_from_countries;
update divers set isNewsFromCurrentLocation = false;
