INSERT INTO `national_federations`
(`id`,
 `deleted`,
 `name`,
 `version`,
 `country_id`)
VALUES
(35,
 FALSE,
 "SLOVENIAN DIVING FEDERATION  (SPZ)",
 2,
 110);

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
 `defaultVisibility`,
 `diverLevel`,
 `diverType`,
 `hasPayed`,
 `isAddFriendsToLogbookEntries`,
 `isNewsFromCurrentLocation`,
 `socialUpdatesVersion`,
 `country_id`,
 `userBalance_id`,
 `federation_id`,
 `primaryPersonalCard_id`,
 `instructor_id`,
 `userpicUrl`,
 `areaOfInterest`,
 `dateLicencePaymentIsDue`,
 `diverRegistrationStatus`,
 `previousRegistrationStatus`,
 `extId`,
 `dateGoldStatusPaymentIsDue`,
 `bot`,
 `phone`,
 `mobileAuthToken`,
 `payedAtLeastOnce`)
VALUES (34,
        now(),
        NULL,
        "racun@spz.si",
        TRUE,
        "SLOVENIAN DIVING FEDERATION  (SPZ)",
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
        "PRIVATE",
        NULL,
        NULL,
        FALSE,
        FALSE,
        FALSE,
        0,
        110,
        NULL,
        35,
        NULL,
        NULL,
        NULL,
        NULL,
        NULL,
        NULL,
        NULL,
        "34",
        NULL,
        FALSE,
        "racun@spz.si",
        NULL,
        FALSE
        );