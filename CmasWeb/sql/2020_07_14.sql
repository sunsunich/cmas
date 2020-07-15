update divers set payedAtLeastOnce = false where diverRegistrationStatus not in ('CMAS_FULL', 'GUEST');
update divers set payedAtLeastOnce = false where diverRegistrationStatus is null;
update divers set payedAtLeastOnce = true where diverRegistrationStatus in ('CMAS_FULL', 'GUEST');