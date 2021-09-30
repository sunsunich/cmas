select d.firstName as 'First Name', d.lastName as 'Other Names', d.email as 'Email',
 c.name as 'Country', f.name as 'Federation', d.diverType as 'Diver Type', d.diverLevel  as 'Diver Level',
 fl.amount as 'Amount Paid (EURO)', fl.recordDate as 'Date and Time Paid', d.diverRegistrationStatus as 'Payment For'
 from divers d inner join countries c on d.country_id = c.id
 inner join fin_log fl on d.id = fl.diver_id
 left outer join national_federations f on d.federation_id = f.id
 where fl.amount > 2.00 and fl.operationType = 'IN' and not (d.email in ('cmasdata.help@gmail.com', 't.petersen@balticfinance.com'))
 and fl.recordDate > '2021-01-01 00:00:00'
 order by fl.recordDate asc