<#import "macro.ftl" as mailer/>
<@mailer.mail title="Your password">
<p>Dear ${diver.firstName} ${diver.lastName},</p>
<p>
    your ${siteName} account has been registered to your e-mail address with the following contents:<br />
    <br />
    Email: ${diver.email}<br />
    Password: ${diver.generatedPassword}<br />
    <br />
    Please login here: <@mailer.href url="${context_path}/login-form.html"/> <br />
    <br/>
    If the link does not open, copy it to the browser command line.<br />
    <br />
    You can change your password under MY PROFILE. <br />
    <br />
    Welcome! <br />
    <br />
    CMAS Aqua link <br />
</p>
</@mailer.mail>        

