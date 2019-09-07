<#import "macro.ftl" as mailer/>
<@mailer.mail title="Registration at AquaLink is successful">
<p>Dear ${user.firstName} ${user.lastName},</p>
<p>
    You have successfully registered at ${siteName}<br>
    <br>

    Welcome! <br/>
    <br/>
    AquaLink <br/>
</p>
</@mailer.mail>

