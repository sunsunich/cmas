INSERT into national_federations (id, deleted, name, version, country_id) values (14, false, 'Turkey', 1, 121);
INSERT INTO divers
(`id`,
 `dateReg`,
 `dob`,
 `email`,
 `enabled`,
 `firstName`,
 `lastAction`,
 `lastName`,
 `locale`,
 `lostPasswdCode`,
 `md5newMail`,
 `mobileLockCode`,
 `newMail`,
 `password`,
 `role`,
 `diverLevel`,
 `diverType`,
 `country_id`,
 `userBalance_id`,
 `federation_id`,
 `primaryPersonalCard_id`
)
VALUES
  (14,
   null,
   null,
   "TurkeyFed@gmail.com",
   true,
   "TurkeyFed",
   NOW(),
   "",
   "tr",
   null,
   null,
   null,
   null,
   "36b1f45f0a95d1624734220892f0e7a9",
   "ROLE_FEDERATION_ADMIN",
   null,
   null,
   121,
   NULL ,
   14,
   null
  );

INSERT INTO divers
(`id`,
 `dateReg`,
 `dob`,
 `email`,
 `enabled`,
 `firstName`,
 `lastAction`,
 `lastName`,
 `locale`,
 `lostPasswdCode`,
 `md5newMail`,
 `mobileLockCode`,
 `newMail`,
 `password`,
 `role`,
 `diverLevel`,
 `diverType`,
 `country_id`,
 `userBalance_id`,
 `federation_id`,
 `primaryPersonalCard_id`
)
VALUES
  (15,
   null,
   null,
   "FranceFed@gmail.com",
   true,
   "FranceFed",
   NOW(),
   "",
   "fr",
   null,
   null,
   null,
   null,
   "36b1f45f0a95d1624734220892f0e7a9",
   "ROLE_FEDERATION_ADMIN",
   null,
   null,
   46,
   NULL ,
   4,
   null
  );

delete from countries where id in (1, 7, 8, 123, 127);
update countries set name = 'Macedonia' WHERE id  = 45;

set @v := (select id FROM personal_cards where
diver_id in ( select id from divers where email = 'dror@diving.org.il')
and cardType = 'PRIMARY');
update personal_cards set number = '13', image = NULL where id = @v;

set @v := (select id FROM personal_cards where
  diver_id in ( select id from divers where email = 'ronny.margodt@nelos.be')
  and cardType = 'PRIMARY');
update personal_cards set number = '12', image = NULL where id = @v;

set @v := (select id FROM personal_cards where
  diver_id in ( select id from divers where email = 'jesper@sportsdykning.dk')
  and cardType = 'PRIMARY');
update personal_cards set number = '14', image = NULL where id = @v;

set @v := (select id FROM personal_cards where
  diver_id in ( select id from divers where email = 'sari.nuotio@sukeltaja.fi')
  and cardType = 'PRIMARY');
update personal_cards set number = '15', image = NULL where id = @v;

set @v := (select id FROM personal_cards where
  diver_id in ( select id from divers where email = 'steffen.scholz@vdst.de')
  and cardType = 'PRIMARY');
update personal_cards set number = '9', image = NULL where id = @v;

update divers set isAddFriendsToLogbookEntries = false WHERE isAddFriendsToLogbookEntries is NULL;
update divers set isNewsFromCurrentLocation = false WHERE isNewsFromCurrentLocation is NULL;
update divers set defaultVisibility = 'PRIVATE' WHERE defaultVisibility is NULL;
update divers set socialUpdatesVersion = 0 WHERE socialUpdatesVersion is NULL;

update personal_cards set image = NULL;



