update user_balances set balance = 0.00 where balance <> 0.00;
INSERT INTO `cmasdata`.`user_balances`
(
  `balance`,
  `discountPercent`,
  `version`)
VALUES
  (
    0.00,
    0.00,
    0);
update divers set userBalance_id = (select count(*) from user_balances) where id = 2 and userBalance_id is null;
INSERT INTO `cmasdata`.`user_balances`
(
  `balance`,
  `discountPercent`,
  `version`)
VALUES
  (
    0.00,
    0.00,
    0);

update divers set userBalance_id = (select count(*) from user_balances) where id = 3 and userBalance_id is null;
INSERT INTO `cmasdata`.`user_balances`
(
  `balance`,
  `discountPercent`,
  `version`)
VALUES
  (
    0.00,
    0.00,
    0);

update divers set userBalance_id = (select count(*) from user_balances) where id = 4 and userBalance_id is null;
INSERT INTO `cmasdata`.`user_balances`
(
  `balance`,
  `discountPercent`,
  `version`)
VALUES
  (
    0.00,
    0.00,
    0);

update divers set userBalance_id = (select count(*) from user_balances) where id = 5 and userBalance_id is null;
INSERT INTO `cmasdata`.`user_balances`
(
  `balance`,
  `discountPercent`,
  `version`)
VALUES
  (
    0.00,
    0.00,
    0);

update divers set userBalance_id = (select count(*) from user_balances) where id = 6 and userBalance_id is null;
INSERT INTO `cmasdata`.`user_balances`
(
  `balance`,
  `discountPercent`,
  `version`)
VALUES
  (
    0.00,
    0.00,
    0);

update divers set userBalance_id = (select count(*) from user_balances) where id = 7 and userBalance_id is null;
INSERT INTO `cmasdata`.`user_balances`
(
  `balance`,
  `discountPercent`,
  `version`)
VALUES
  (
    0.00,
    0.00,
    0);

update divers set userBalance_id = (select count(*) from user_balances) where id = 8 and userBalance_id is null;
INSERT INTO `cmasdata`.`user_balances`
(
  `balance`,
  `discountPercent`,
  `version`)
VALUES
  (
    0.00,
    0.00,
    0);

update divers set userBalance_id = (select count(*) from user_balances) where id = 9 and userBalance_id is null;
INSERT INTO `cmasdata`.`user_balances`
(
  `balance`,
  `discountPercent`,
  `version`)
VALUES
  (
    0.00,
    0.00,
    0);

update divers set userBalance_id = (select count(*) from user_balances) where id = 10 and userBalance_id is null;
INSERT INTO `cmasdata`.`user_balances`
(
  `balance`,
  `discountPercent`,
  `version`)
VALUES
  (
    0.00,
    0.00,
    0);

update divers set userBalance_id = (select count(*) from user_balances) where id = 11 and userBalance_id is null;
INSERT INTO `cmasdata`.`user_balances`
(
  `balance`,
  `discountPercent`,
  `version`)
VALUES
  (
    0.00,
    0.00,
    0);

update divers set userBalance_id = (select count(*) from user_balances) where id = 12 and userBalance_id is null;
INSERT INTO `cmasdata`.`user_balances`
(
  `balance`,
  `discountPercent`,
  `version`)
VALUES
  (
    0.00,
    0.00,
    0);

update divers set userBalance_id = (select count(*) from user_balances) where id = 13 and userBalance_id is null;
INSERT INTO `cmasdata`.`user_balances`
(
  `balance`,
  `discountPercent`,
  `version`)
VALUES
  (
    0.00,
    0.00,
    0);

update divers set userBalance_id = (select count(*) from user_balances) where id = 14 and userBalance_id is null;
INSERT INTO `cmasdata`.`user_balances`
(
  `balance`,
  `discountPercent`,
  `version`)
VALUES
  (
    0.00,
    0.00,
    0);

update divers set userBalance_id = (select count(*) from user_balances) where id = 15 and userBalance_id is null;
INSERT INTO `cmasdata`.`user_balances`
(
  `balance`,
  `discountPercent`,
  `version`)
VALUES
  (
    0.00,
    0.00,
    0);

update divers set userBalance_id = (select count(*) from user_balances) where id = 16 and userBalance_id is null;
INSERT INTO `cmasdata`.`user_balances`
(
  `balance`,
  `discountPercent`,
  `version`)
VALUES
  (
    0.00,
    0.00,
    0);

update divers set userBalance_id = (select count(*) from user_balances) where id = 17 and userBalance_id is null;
INSERT INTO `cmasdata`.`user_balances`
(
  `balance`,
  `discountPercent`,
  `version`)
VALUES
  (
    0.00,
    0.00,
    0);

update divers set userBalance_id = (select count(*) from user_balances) where id = 18 and userBalance_id is null;
INSERT INTO `cmasdata`.`user_balances`
(
  `balance`,
  `discountPercent`,
  `version`)
VALUES
  (
    0.00,
    0.00,
    0);

update divers set userBalance_id = (select count(*) from user_balances) where id = 19 and userBalance_id is null;

update divers set diverLevel = 'ONE_STAR' where diverLevel is null and role = 'ROLE_DIVER' ;
update personal_cards set diverLevel = 'ONE_STAR' where diverLevel is null