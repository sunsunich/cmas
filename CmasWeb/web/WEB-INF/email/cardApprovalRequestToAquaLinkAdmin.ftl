<#import "macro.ftl" as mailer/>
<@mailer.mail title="Card approval requestT submitted">
    <p>Dear AquaLink support,</p>
    <p>
        Please contact CMAS to verify card approval request with id = ${id} <br/>
        Please check the following items
    <ul>
        <li>
            creator email: ${diver.email}
        </li>
        <li>
            front image url: ${frontImage}
        </li>
        <li>
            back image url: ${backImage}
        </li>
    </ul>
    <br/>
    Sincerely yours, <br/>
    AquaLink <br/>
    </p>
</@mailer.mail>

