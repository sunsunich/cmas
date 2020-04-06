update divers set bot = 0;
update divers set bot = 1 where firstName like '%Bot' and federation_id is null;

