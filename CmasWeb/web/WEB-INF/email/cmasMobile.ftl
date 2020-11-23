<#import "macro.ftl" as mailer/>
<@mailer.mail title="Your password">
<p>Dear ${diver.firstName} ${diver.lastName},</p>
<p>
    We are happy to announce the release of an improved CMAS E-cards mobile App for Apple (iOS) devices!<br />
    We will also add support for Android devices soon<br />
    <br />
    To login to CMAS E-cards mobile App please open this email on your mobile device and tap on the link:<br />
    <@mailer.href url="cmas://login?token=${diver.mobileAuthToken}"/> <br />
    <br />
    For iOS users only: if the link does not open the CMAS E-cards mobile App, copy the link, and then do "Paste and Go" in the Safari browser address line on your iOS device.<br />
    <br />
    Sincerely yours,<br />
    AquaLink <br />
</p>
</@mailer.mail>        

