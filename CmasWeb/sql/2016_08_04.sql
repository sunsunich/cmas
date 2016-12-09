update divers set socialUpdatesVersion = 0;
ALTER TABLE logbook_entries
CHANGE COLUMN `note` `note` VARCHAR(2048) NULL DEFAULT NULL;
