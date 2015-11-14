<#import "macro.ftl" as mailer/>
<@mailer.mail title="Регистрация успешно произведена">
<p style="color: #3a3a3a; font-size: 18px;">Здравствуйте, ${user.username}!</p>
<p>
    Вы успешно зарегистрировались cmas.org<br>
    <br>
</p>
</@mailer.mail>

