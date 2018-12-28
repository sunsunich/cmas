INSERT INTO `national_federations`
(`id`,
 `deleted`,
 `name`,
 `version`,
 `country_id`)
VALUES
  (16,
   false,
   "Serbia",
   1,
   106);

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
  (17,
    null,
    null,
    "bozana.ostojic@gmail.com",
    true,
    "Bozana",
    NOW(),
    "Ostojic",
    "sr_RS",
    null,
    null,
    null,
    null,
    "36b1f45f0a95d1624734220892f0e7a9",
    "ROLE_FEDERATION_ADMIN",
    null,
    null,
    106,
    NULL,
    16,
    null
  );

UPDATE `divers` SET `defaultVisibility`='PRIVATE', `isAddFriendsToLogbookEntries`=0, `isNewsFromCurrentLocation`=0, `socialUpdatesVersion`=0 WHERE `id`='17';

