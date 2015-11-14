<#import "macro.ftl" as mailer/>
<@mailer.mail title="Восстановление пароля">
<p style="color: #3a3a3a; font-size: 18px;">Здравствуйте, ${user.username}!</p>
<p style="font-size: 14px;">    
    Для изменения пароля проследуйте по ссылке:<br>
    <@mailer.href url="${context_path}/toChangePasswd.html?code=${user.lostPasswdCode}"/>
    <br>
    Если указанная ссылка не работает, то скопируйте ее и запустите в строке браузера.
</p>
</@mailer.mail>        

