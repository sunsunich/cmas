INSERT INTO `national_federations`
(`id`,
 `deleted`,
 `name`,
 `version`,
 `country_id`)
VALUES
(27,
 FALSE,
 "MAURITIAN SCUBA DIVING ASSOCIATION",
 1,
 79);

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
(28,
 now(),
 NULL,
 "msda@intnet.mu",
 TRUE,
 "MAURITIAN SCUBA DIVING ASSOCIATION",
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
 79,
 NULL,
 27,
 NULL,
 NULL,
 'PRIVATE',
 FALSE,
 FALSE,
 0,
 NULL,
 NULL,
 NULL);
