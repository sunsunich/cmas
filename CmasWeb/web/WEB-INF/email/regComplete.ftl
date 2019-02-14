<#import "macro.ftl" as mailer/>
<@mailer.mail title="Registration at CMAS Aqua link is successful">
<p>Dear ${user.firstName} ${user.lastName},</p>
<p>
    You have successfully registered at ${siteName}<br>
    <br>

    Welcome! <br/>
    <br/>
    CMAS Aqua link <br/>
</p>
</@mailer.mail>

