<#import "macro.ftl" as mailer/>
<@mailer.mail title="Поступление денег на счет cmas.org">

<p style="color: #3a3a3a; font-size: 18px;">Здравствуйте, ${invoice.user.username}!</p>
<p style="color:#3a3a3a;  font-size:14px; text-indent:0; text-align:left; margin-bottom:0">
    Ваш платеж от ${date} через ${invoiceType} на сумму ${invoice.amount} $ успешно проведен!
</p>

</@mailer.mail>

