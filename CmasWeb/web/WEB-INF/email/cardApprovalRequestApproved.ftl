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
        Sincerely yours, <br/>
        AquaLink <br/>
    </p>
</@mailer.mail>

