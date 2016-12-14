<#import "macro.ftl" as mailer/>

<@mailer.mail title="Rigastration in CMASDATA">

<div>
    <div class="adM"><p>
    </p></div>
    <p>Dear <span class="il">CMASDATA</span> User,<br></p>

    <p>your <span class="il">CMASDATA</span> account has been registered to your e-mail&nbsp; address with the following
        contents:<br></p>
    <table border="0" cellpadding="3">

        <tbody>
        <tr>
            <td><span class="il">CMASDATA</span> ID:</td>
            <td><a href="mailto:${reg.email}" target="_blank">${reg.email}</a></td>
        </tr>
        <tr>
            <td>Country:</td>
            <td>${country}</td>
        </tr>
        </tbody>
    </table>
    <p>Please confirm your registration y following this link<br />
        <@mailer.href url="${context_path}/regConfirm.html?regId=${reg.id}&sec=${reg.md5}"/>
    </p>

    <p>Welcome!</p>

    <p><span class="il">CMASDATA</span><br></p>

    <div class="yj6qo"></div>
    <div class="adL">
        <p></p></div>
</div>

</@mailer.mail>

