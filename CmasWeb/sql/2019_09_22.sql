update divers d
set dateGoldStatusPaymentIsDue =
        DATE_ADD((select ir.createDate
                  from invoice i
                           inner join insurance_requests ir on ir.invoice_id = i.id
                  where i.invoiceStatus = 0
                    and d.id = ir.diver_id), INTERVAL 1 year);
;


