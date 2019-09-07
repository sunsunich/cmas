<#import "macro.ftl" as mailer/>
<@mailer.mail title="Friend request">
<p>Dear ${request.to.firstName} ${request.to.lastName},</p>
<p>${request.from.firstName} ${request.from.lastName} requests you to verify the dive made on ${diveDate}.</p>
<p>
    To verify or decline this request log in to your AquaLink account, go to "FRIENDS" section:<br />
    <br />
    <@mailer.href url="${context_path}/secure/social.html"/><br />
    <br />
    If the link does not open, copy it to the browser command line.<br />
    <br />
    Sincerely yours, <br />
    AquaLink <br />
</p>
</@mailer.mail>        

