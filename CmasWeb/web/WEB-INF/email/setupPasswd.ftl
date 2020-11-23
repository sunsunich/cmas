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
    You can change your password after the first login. <br />
    <br />
    <br />
    Please kindly note that CMAS E-cards mobile App currently works only on Apple (iOS) devices<br />
    We will add support for Android devices soon<br />
    <br />
    To login to CMAS E-cards mobile App please open this email on your mobile device and tap on the link:<br />
    <@mailer.href url="cmas://login?token=${diver.mobileAuthToken}"/> <br />
    <br />
    For iOS users only: if the link does not open the CMAS E-cards mobile App, copy the link, and then do "Paste and Go" in the Safari browser address line on your iOS device.<br />
    <br />
    Welcome! <br />
    <br />
    AquaLink <br />
</p>
</@mailer.mail>        

