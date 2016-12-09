<#import "macro.ftl" as mailer/>
<@mailer.mail title="Payment at CMAS failed">
<p>Dear ${invoice.diver.firstName} ${invoice.diver.lastName},</p>
<p>
    your one time payment of membership fee failed! Please try again. <br />
</p>
</@mailer.mail>

