update divers set diverRegistrationStatus = 'CMAS_FULL', previousRegistrationStatus = 'CMAS_BASIC'
where diverRegistrationStatus = 'CMAS_BASIC' and previousRegistrationStatus = 'CMAS_FULL';

update divers set diverRegistrationStatus = 'GUEST' , previousRegistrationStatus = 'DEMO'
where diverRegistrationStatus = 'INACTIVE' and previousRegistrationStatus = 'GUEST'