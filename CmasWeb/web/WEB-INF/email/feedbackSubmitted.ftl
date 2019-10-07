<#import "macro.ftl" as mailer/>
<@mailer.mail title="Feedback submitted">
    <p>Dear AquaLink support,</p>
    <p>
        It is time to review feedback with id = ${id} <br/>
        Please check the following items
    <ul>
        <li>
            creator email: ${diver.email}
        </li>
        <li>
            text: ${text}
        </li>
        <li>
            image1 url: ${imageUrl1}
        </li>
        <li>
            image2 url: ${imageUrl2}
        </li>
        <li>
            spot id: ${spotId}
        </li>
        <li>
            logbook entry id: ${logbookEntryId}
        </li>
    </ul>
    <br/>
    Sincerely yours, <br/>
    AquaLink <br/>
    </p>
</@mailer.mail>

