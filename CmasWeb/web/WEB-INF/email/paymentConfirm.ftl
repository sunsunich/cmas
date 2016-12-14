<#import "macro.ftl" as mailer/>
<@mailer.mail title="Payment at CMASDATA is successful">
<p>Dear ${invoice.diver.firstName} ${invoice.diver.lastName},</p>
<p>
    your one time payment of membership fee was successful! <br />
</p>
</@mailer.mail>


