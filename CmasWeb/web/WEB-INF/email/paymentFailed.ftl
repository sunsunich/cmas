<#import "macro.ftl" as mailer/>
<@mailer.mail title="Payment at CMASDATA failed">
<p>Dear ${invoice.diver.firstName} ${invoice.diver.lastName},</p>
<p>
    your payment for membership fee has failed! Please try again. <br />
</p>
</@mailer.mail>

