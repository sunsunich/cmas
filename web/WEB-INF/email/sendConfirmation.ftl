<#import "macro.ftl" as mailer/>
<@mailer.mail title="Подтвердить регистрацию нового пользователя">
<p>${reg.username} запросил регистрацию в системе</p>
<p>    
    Детали:
    ${reg.shopName}<br />
    ${reg.city}<br />
    ${reg.webAddress}<br />
    ${reg.email}<br />
</p>
<p>
    Чтобы подтвердить регистрацию, перейдите по ссылке и подтвердите:<br />
    <@mailer.href url="${context_path}/admin/registration/readyToCreate.html"/><br />
</p>
</@mailer.mail>        

