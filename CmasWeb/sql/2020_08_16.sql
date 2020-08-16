UPDATE national_federations AS nf, divers AS d
SET nf.email = d.email
WHERE nf.email is null and d.role = 'ROLE_FEDERATION_ADMIN' and nf.id = d.federation_id;

UPDATE card_approval_requests AS c, national_federations AS nf
SET c.issuingFederation_id = nf.id
WHERE c.issuingFederation_id is null
  and c.issuingCountry_id not in (120, 107, 3, 12, 26, 80, 43, 61, 65, 74, 95, 32, 116, 129, 17, 23, 34, 48, 49, 53, 60, 96, 114)
  and c.issuingCountry_id = nf.country_id;



