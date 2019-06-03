set @emailToDelete = 'mony.khy@gmail.com';

select @idToDelete := (select id from divers where email = @emailToDelete);
select @balanceToDelete := (select userBalance_id from divers where email = @emailToDelete);

update divers set primaryPersonalCard_id = null, userBalance_id = null  where id = @idToDelete;

delete from user_balances where id = @balanceToDelete;

delete from personal_cards where diver_id = @idToDelete;

delete from user_events where diver_id = @idToDelete;

delete from divers where id = @idToDelete;