<#import "macro.ftl" as mailer/>
<@mailer.mail title="CMAS E-cards mobile App">
    <p>Dear ${diver.firstName} ${diver.lastName},</p>
    <p>
        We are happy to announce the release of the improved CMAS E-cards mobile App for Apple (iOS) devices!<br/>
        We will also add support for Android devices soon.<br/>
        <br/>
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
            For iOS users only: if the links above do not open the CMAS E-cards mobile App, copy the link, and then do "Paste
            and Go" in the Safari browser address line on your iOS device.<br/>
            <br/>
        </li>
    </ol>
    Sincerely yours,<br/>
    AquaLink <br/>
    </p>
    <p>
        If you do not want to receive emails with updates or news from CMAS AquaLink, please open the link below: <br/>
        <@mailer.href url="${context_path}/unsubscribe.html?unsubscribeToken=${unsubscribeToken}"/>
    </p>
</@mailer.mail>        

