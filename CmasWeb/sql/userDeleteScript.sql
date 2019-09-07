set @emailToDelete = 'mony.khy@gmail.com';

select @idToDelete := (select id from divers where email = @emailToDelete);
select @balanceToDelete := (select userBalance_id from divers where id = @idToDelete);

update divers set primaryPersonalCard_id = null, userBalance_id = null  where id = @idToDelete;

delete from user_balances where id = @balanceToDelete;

delete from personal_cards where diver_id = @idToDelete;

delete from user_events where diver_id = @idToDelete;

delete from divers where id = @idToDelete;

# mass users delete, use with backup!!!
# returns only 40-50 ids
select @idToDelete := (select GROUP_CONCAT(id) from divers
                       where not (email like '%@mailinator.com') and role = 'ROLE_DIVER' || role <> 'ROLE_DIVER');

select @balanceToDelete := (select GROUP_CONCAT(userBalance_id) from divers where FIND_IN_SET(id, @idToDelete));

update divers set primaryPersonalCard_id = null, userBalance_id = null where FIND_IN_SET(id, @idToDelete);

delete from user_balances where FIND_IN_SET(id, @balanceToDelete);

delete from personal_cards where FIND_IN_SET(diver_id, @idToDelete);

delete from user_events where FIND_IN_SET(diver_id, @idToDelete);

select @invoiceToDelete := (select GROUP_CONCAT(id) from invoice where FIND_IN_SET(diver_id, @idToDelete));
delete from invoice_requested_paid_features where FIND_IN_SET(invoiceId, @invoiceToDelete);
delete from invoice_paid_for_divers where FIND_IN_SET(invoiceId, @invoiceToDelete);
delete from invoice where FIND_IN_SET(id, @invoiceToDelete);

delete from logbook_buddie_requests where FIND_IN_SET(from_id, @idToDelete);
delete from logbook_buddie_requests where FIND_IN_SET(to_id, @idToDelete);
# review this one
update logbook_entries set instructor_id = null where FIND_IN_SET(instructor_id, @idToDelete);
select @logbookToDelete := (select GROUP_CONCAT(id) from logbook_entries where FIND_IN_SET(diver_id, @idToDelete));

delete from logbook_entry_buddies where FIND_IN_SET(diverId, @idToDelete);
delete from logbook_entry_buddies where FIND_IN_SET(logbookEntryId, @logbookToDelete);
delete from logbook_entries where FIND_IN_SET(id, @logbookToDelete);

delete from diver_friend_requests where FIND_IN_SET(from_id, @idToDelete);
delete from diver_friend_requests where FIND_IN_SET(to_id, @idToDelete);

update divers set instructor_id = null where FIND_IN_SET(instructor_id, @idToDelete);
delete from divers where FIND_IN_SET(id, @idToDelete);