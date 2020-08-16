<#import "macro.ftl" as mailer/>
<@mailer.mail title="Your certificate approval request was declined">
    <p>Dear ${diver.firstName} ${diver.lastName},</p>
    <p>
        Congratulations! your certificate has been approved by ${federationName}.<br/>
    </p>
    <p>
        Your new card:
        <img src="${cardUrl}" alt="new card"/>
    </p>
    <p>
        Your AquaLink account status is: ${statusStr} <br/>
        <br/>
        Sincerely yours, <br/>
        AquaLink <br/>
    </p>
</@mailer.mail>

