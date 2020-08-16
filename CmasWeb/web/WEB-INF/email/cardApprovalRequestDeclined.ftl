<#import "macro.ftl" as mailer/>
<@mailer.mail title="Your certificate approval request was declined">
    <p>Dear ${diver.firstName} ${diver.lastName},</p>
    <p>
        We regret to inform you that you certificate approval request was declined by your National Federation
        (${federation.name}).<br/>
        Please contact ${federation.name} if you would like to dispute their decision.<br/>
    </p>
    <p>
        ${federation.name} contact email: ${federation.email}
    </p>
    <p>
    Details of your certificate approval request:
    <ul>
        <li>
            create date: ${createDate}
        </li>
    Follow the links to view submitted images:
        <li>
            front image: ${frontImage}
        </li>
        <li>
            back image: ${backImage}
        </li>
    </ul>
    <br/>
    Sincerely yours, <br/>
    AquaLink <br/>
    </p>
</@mailer.mail>

