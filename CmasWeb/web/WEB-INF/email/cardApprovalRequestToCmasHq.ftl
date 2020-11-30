<#import "macro.ftl" as mailer/>
<@mailer.mail title="Card approval request submitted">
    <p>Dear CMAS HQ,</p>
    <p>
        Diver ${diver.firstName} ${diver.lastName} requested approval of their CMAS certificate.
    </p>
    <p>
        CMAS AquaLink was not able to determine which National Federation it belongs to.
    </p>
    <p>
        Please review the following items and pass them to the appropriate National Federation for approval
    <ul>
        <li>
            creator email: ${diver.email}
        </li>
        <li>
            to view the front image of certificate open the link below:<br/>
            <@mailer.href url="${frontImage}"/>
        </li>
        <li>
            to view the back image of certificate open the link below:<br/>
            <@mailer.href url="${backImage}"/>
        </li>
    </ul>
    <p>
        Once you received a response from the National Federation please come back to CMAS AquaLink by writing us an
        email to:<br/>
        ${returnEmail}<br/>
        <br/>
        Please do not reply to this email. Use ${returnEmail} for communication with CMAS AquaLink.
    </p>
    <p>
        Thank you very much for your cooperation.
    </p>
    Sincerely yours, <br/>
    AquaLink <br/>
    </p>
</@mailer.mail>

