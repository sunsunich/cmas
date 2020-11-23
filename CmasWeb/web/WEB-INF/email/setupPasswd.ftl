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
    To use the CMAS E-cards mobile App please follow the steps below:<br/>
    <ol>
        <li>
            open this email on your mobile device
        </li>
        <li>
            install the CMAS E-cards mobile App from Apple Store:<br/>
            <@mailer.href url="itms-apps://apps.apple.com/app/id1482247868"/> <br/>
        </li>
        <li>
            once the CMAS E-cards mobile App is installed login by opening this email on your mobile device and tapping
            on the link:<br/>
            <@mailer.href url="cmas://login?token=${diver.mobileAuthToken}"/> <br/>
            <br/>
            For iOS users only: if the links above do not open the CMAS E-cards mobile App, copy the link, and then do
            "Paste and Go" in the Safari browser address line on your iOS device.<br/>
            <br/>
        </li>
    </ol>
    <br />
    Welcome! <br />
    <br />
    AquaLink <br />
</p>
</@mailer.mail>        

