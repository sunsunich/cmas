INSERT INTO paid_feature (`id`, `deleted`, `name`, `version`, `descriptionCode`, `price`) VALUES ('1', 0, 'cmas.face.payment.cmasLicence.name', 0, 'cmas.face.payment.cmasLicence.description', 1);
INSERT INTO paid_feature (`id`, `deleted`, `name`, `version`, `descriptionCode`, `price`) VALUES ('2', 0, 'cmas.face.payment.insurance.name', 0, 'cmas.face.payment.insurance.description', 1);
update user_balances set discountPercent = 0;
ALTER TABLE registration
  CHANGE COLUMN `password` `password` VARCHAR(255) NULL ;
