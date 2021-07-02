<#import "macro.ftl" as mailer/>
<@mailer.mail title="Registration in AquaLink">
    <p>Dear ${reg.firstName} ${reg.lastName},</p>
    <p>
        your ${siteName} account has been registered to your e-mail address:<br/>
        <br/>
        ${reg.email}<br/>
        <br/>
        Please confirm your registration by following this link<br/>
        <@mailer.href url="${context_path}/regConfirm.html?regId=${reg.id}&sec=${reg.md5}"/> <br/>
        <br/>
        If the link does not open, copy it to the browser command line.<br/>
        <br/>
        Welcome! <br/>
        <br/>
        Please accept our apology if you have received the registration email more than once or received it with a delay.<br/>
        The issue with emails being delayed is now resolved.<br/>
        <br/>
        Sincerely yours, <br/>
        AquaLink <br/>
    </p>
</@mailer.mail>

