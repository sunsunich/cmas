<#import "macro.ftl" as mailer/>
<@mailer.mail title="Неуспешное поступление денег на счет cmasdata.org">

<p style="color: #3a3a3a; font-size: 18px;">
    
    Платеж пользователя ${invoice.user.username} с номером ${invoice.externalInvoiceNumber} от ${date} через ${invoiceType} на сумму ${invoice.amount} $ не прошел!
</p>

</@mailer.mail>

