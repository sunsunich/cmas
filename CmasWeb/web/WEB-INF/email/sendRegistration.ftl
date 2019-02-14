<#import "macro.ftl" as mailer/>
<@mailer.mail title="Registration in CMAS Aqua link">
<p>Dear ${reg.firstName} ${reg.lastName},</p>
<p>
    your ${siteName} account has been registered to your e-mail address:<br/>
    <br/>
    ${reg.email}<br/>
    <br/>
    Please confirm your registration by following this link<br/>
    <@mailer.href url="${context_path}/regConfirm.html?regId=${reg.id}&sec=${reg.md5}"/> <br/>
    <br/>
    If the link does not open, copy it to the browser command line.<br />
    <br />
    Welcome! <br/>
    <br/>
    CMAS Aqua link <br/>
</p>
</@mailer.mail>

