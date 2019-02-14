<#import "macro.ftl" as mailer/>
<@mailer.mail title="E-mail change">
<p>Dear ${user.firstName} ${user.lastName},</p>
<p>
    You have requested the e-mail address change:<br />
    <br />
    New email: ${user.newMail} <br />
    <br />
    Please confirm the e-mail address change by following the link:<br />
    <@mailer.href url="${context_path}/changeEmail.html?sec=${user.md5newMail}"/> <br />
    <br />
    If the link does not open, copy it to the browser command line.<br />
    <br />
    Sincerely yours, <br />
    CMAS Aqua link <br />
</p>
</@mailer.mail>

