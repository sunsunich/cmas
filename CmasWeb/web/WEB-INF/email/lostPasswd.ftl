<#import "macro.ftl" as mailer/>
<@mailer.mail title="Password recovery">
<p>Dear ${user.firstName} ${user.lastName},</p>
<p>
    To change your password please follow the link:<br />
    <br />
    <@mailer.href url="${context_path}/toChangePasswd.html?code=${user.lostPasswdCode}"/><br />
    <br />
    If the link does not open, copy it to the browser command line.<br />
    <br />
    Sincerely yours, <br />
    CMASDATA <br />
</p>
</@mailer.mail>        

