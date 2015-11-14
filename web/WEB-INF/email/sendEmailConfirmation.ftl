<#import "macro.ftl" as mailer/>
<@mailer.mail title="email change">
<p style="color: #3a3a3a; font-size: 18px;">Здравствуйте, ${user.username}!</p>
<p style="color: #3a3a3a; font-size: 14px;">
    Вы запросили изменение текущего e-mail на ${user.newMail}<br>
    <br>
    Подтвердите это изменение перейдя по указанной ссылке:<br>
    <@mailer.href url="${context_path}/changeEmail.html?sec=${user.md5newMail}"/>
    <br>
    Если указанная ссылка не работает, то скопируйте ее и запустите в строке браузера.
</p>
</@mailer.mail>

