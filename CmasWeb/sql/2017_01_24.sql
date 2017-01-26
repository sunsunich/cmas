delete FROM logbook_entry_buddies;
delete FROM logbook_buddie_requests;
delete FROM logbook_entries;
delete FROM dive_spots where isApproved = false;