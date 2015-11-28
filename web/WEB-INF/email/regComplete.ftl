<#import "macro.ftl" as mailer/>
<@mailer.mail title="Registration at CMAS is successful">
<p style="color: #3a3a3a; font-size: 18px;">Dear, ${user.email}!</p>
<p>
    You have successfully registered at cmas.org<br>
    <br>
</p>
</@mailer.mail>

