

update divers set isAddFriendsToLogbookEntries = false WHERE isAddFriendsToLogbookEntries is NULL;
update divers set isNewsFromCurrentLocation = false WHERE isNewsFromCurrentLocation is NULL;
update divers set defaultVisibility = 'PRIVATE' WHERE defaultVisibility is NULL;
update divers set socialUpdatesVersion = 0 WHERE socialUpdatesVersion is NULL;

update personal_cards set image = NULL;

update divers set socialUpdatesVersion = 0;
update personal_cards set number = REPLACE(number, 'RUS/F01/', 'RUS/FO1/')
