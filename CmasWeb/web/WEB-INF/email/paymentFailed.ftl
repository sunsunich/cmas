<#import "macro.ftl" as mailer/>
<@mailer.mail title="Payment to CMAS failed">
<p>Dear ${invoice.diver.firstName} ${invoice.diver.lastName},</p>
<p>
    your payment for membership fee has failed! Please try again. <br />
    <br />
    Sincerely yours, <br />
    AquaLink <br />
</p>
</@mailer.mail>

