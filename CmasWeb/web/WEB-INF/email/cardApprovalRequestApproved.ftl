<#import "macro.ftl" as mailer/>
<@mailer.mail title="Your certificate approval request was declined">
    <p>Dear ${diver.firstName} ${diver.lastName},</p>
    <p>
        Congratulations! your certificate has been approved by ${federationName}.<br/>
    </p>
    <p>
        Please follow the link below to view your new card: <br/>
        ${cardUrl}
    </p>
    <p>
        Your AquaLink account status is: ${statusStr} <br/>
        <br/>
        <br />
        Please kindly note that CMAS E-cards mobile App currently works only on Apple (iOS) devices<br />
        We will add support for Android devices soon<br />
        <br />
        To login to CMAS E-cards mobile App please open this email on your mobile device and tap on the link:<br />
        <@mailer.href url="https://www.cmasdata.org/login?token=${diver.mobileAuthToken}"/> <br />
        <br />
        For iOS users only: if the link does not open the CMAS E-cards mobile App, copy the link, and then do "Paste and Go" in the Safari browser address line on your iOS device.<br />
        <br />
        Sincerely yours, <br/>
        AquaLink <br/>
    </p>
</@mailer.mail>

