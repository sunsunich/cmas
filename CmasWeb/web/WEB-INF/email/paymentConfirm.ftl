<#import "macro.ftl" as mailer/>
<@mailer.mail title="Payment at CMAS Aqua link is successful">
<p>Dear ${invoice.diver.firstName} ${invoice.diver.lastName},</p>
<p>
    your payment for membership fee was successful! <br />
    <br />
    Sincerely yours, <br />
    CMAS Aqua link <br />
</p>
</@mailer.mail>


