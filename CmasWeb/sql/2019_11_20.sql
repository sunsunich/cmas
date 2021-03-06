INSERT INTO `national_federations`
(`id`,
 `deleted`,
 `name`,
 `version`,
 `country_id`)
VALUES
(30,
 FALSE,
 "INDONESIAN SUBAQUATIC SPORT ASSOCIATION  (POSSIISSA)",
 1,
 55);

INSERT INTO `divers`
(`id`,
 `dateReg`,
 `dob`,
 `email`,
 `enabled`,
 `firstName`,
 `generatedPassword`,
 `lastAction`,
 `lastName`,
 `locale`,
 `lostPasswdCode`,
 `md5newMail`,
 `mobileLockCode`,
 `newMail`,
 `password`,
 `role`,
 `userpic`,
 `diverLevel`,
 `diverType`,
 `country_id`,
 `userBalance_id`,
 `federation_id`,
 `primaryPersonalCard_id`,
 `instructor_id`,
 `defaultVisibility`,
 `isAddFriendsToLogbookEntries`,
 `isNewsFromCurrentLocation`,
 `socialUpdatesVersion`,
 `diverRegistrationStatus`,
 `dateLicencePaymentIsDue`,
 `previousRegistrationStatus`

)
VALUES
(31,
 now(),
 NULL,
 "pbpossi1@gmail.com",
 TRUE,
 "INDONESIAN SUBAQUATIC SPORT ASSOCIATION  (POSSIISSA)",
 NULL,
 now(),
 "",
 "en-us",
 NULL,
 NULL,
 NULL,
 NULL,
 "36b1f45f0a95d1624734220892f0e7a9",
 "ROLE_FEDERATION_ADMIN",
 NULL,
 NULL,
 NULL,
 55,
 NULL,
 30,
 NULL,
 NULL,
 'PRIVATE',
 FALSE,
 FALSE,
 0,
 NULL,
 NULL,
 NULL);
