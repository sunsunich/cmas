update dive_spots set isApproved = true;
update dive_spots set isAutoGeoLocation = false;

INSERT into national_federations (id, deleted, name, version, country_id) values (8, false, 'Israel', 1, 59);
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
  (6,
   null,
   null,
   "IsraelFed@gmail.com",
   true,
   "IsraelFed",
   NOW(),
   "",
   "il",
   null,
   null,
   null,
   null,
   "36b1f45f0a95d1624734220892f0e7a9",
   "ROLE_FEDERATION_ADMIN",
   null,
   null,
   59,
   NULL ,
   8,
   null
  );

INSERT into national_federations (id, deleted, name, version, country_id) values (9, false, 'Ireland', 1, 58);
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
  (7,
   null,
   null,
   "IrelandFed@gmail.com",
   true,
   "IrelandFed",
   NOW(),
   "",
   "ie",
   null,
   null,
   null,
   null,
   "36b1f45f0a95d1624734220892f0e7a9",
   "ROLE_FEDERATION_ADMIN",
   null,
   null,
   58,
   NULL ,
   9,
   null
  );

INSERT into national_federations (id, deleted, name, version, country_id) values (10, false, 'Tunisia', 1, 120);
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
  (8,
   null,
   null,
   "TunisiaFed@gmail.com",
   true,
   "TunisiaFed",
   NOW(),
   "",
   "tn",
   null,
   null,
   null,
   null,
   "36b1f45f0a95d1624734220892f0e7a9",
   "ROLE_FEDERATION_ADMIN",
   null,
   null,
   120,
   NULL ,
   10,
   null
  );

INSERT into national_federations (id, deleted, name, version, country_id) values (11, false, 'Denmark', 1, 36);
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
  (9,
   null,
   null,
   "DenmarkFed@gmail.com",
   true,
   "DenmarkFed",
   NOW(),
   "",
   "dk",
   null,
   null,
   null,
   null,
   "36b1f45f0a95d1624734220892f0e7a9",
   "ROLE_FEDERATION_ADMIN",
   null,
   null,
   36,
   NULL ,
   11,
   null
  );

INSERT into national_federations (id, deleted, name, version, country_id) values (12, false, 'Finland', 1, 44);
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
  (10,
   null,
   null,
   "FinlandFed@gmail.com",
   true,
   "FinlandFed",
   NOW(),
   "",
   "fi",
   null,
   null,
   null,
   null,
   "36b1f45f0a95d1624734220892f0e7a9",
   "ROLE_FEDERATION_ADMIN",
   null,
   null,
   44,
   NULL ,
   12,
   null
  );

INSERT into national_federations (id, deleted, name, version, country_id) values (13, false, 'Portugal', 1, 99);
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
  (11,
   null,
   null,
   "presidencia@fpas.pt",
   true,
   "Ricardo",
   NOW(),
   "José",
   "pt",
   null,
   null,
   null,
   null,
   "36b1f45f0a95d1624734220892f0e7a9",
   "ROLE_FEDERATION_ADMIN",
   null,
   null,
   99,
   NULL ,
   13,
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
  (12,
   null,
   null,
   "GermanyFed@gmail.com",
   true,
   "GermanyFed",
   NOW(),
   "",
   "de",
   null,
   null,
   null,
   null,
   "36b1f45f0a95d1624734220892f0e7a9",
   "ROLE_FEDERATION_ADMIN",
   null,
   null,
   6,
   NULL ,
   3,
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
  (13,
   null,
   null,
   "BelgiumFed@gmail.com",
   true,
   "BelgiumFed",
   NOW(),
   "",
   "be",
   null,
   null,
   null,
   null,
   "36b1f45f0a95d1624734220892f0e7a9",
   "ROLE_FEDERATION_ADMIN",
   null,
   null,
   17,
   NULL ,
   7,
   null
  );

update divers set isAddFriendsToLogbookEntries = false;
update divers set isNewsFromCurrentLocation = false;
update divers set defaultVisibility = 'PRIVATE';
update divers set socialUpdatesVersion = 0;
update countries set code = 'PRT' where code = 'POR';
update countries set code = 'DNK' where code = 'DEN';
update countries set code = 'DEU' where code = 'GER';

INSERT INTO personal_cards
(`id`,
 `cardType`,
 `diverLevel`,
 `diverType`,
 `federationName`,
 `image`,
 `number`,
 `athlete_id`,
 `diver_id`)
  SELECT null, 'PRIMARY', 'TWO_STAR', 'DIVER', null, null, 1, null, id
  FROM   divers
  WHERE  email = 'anna.arzhanova@gmail.com';
SET @v1 := (select id from divers where email = 'anna.arzhanova@gmail.com');

update divers set primaryPersonalCard_id =
(select id FROM personal_cards where cardType='PRIMARY' and diver_id = @v1)
where email = 'anna.arzhanova@gmail.com'