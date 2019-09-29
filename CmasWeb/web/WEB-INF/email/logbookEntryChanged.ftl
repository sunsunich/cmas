<#import "macro.ftl" as mailer/>
<@mailer.mail title="Logbook entry has changed">
    <p>Dear AquaLink support,</p>
    <p>
        It is time to moderate logbook entry with id = ${id} <br/>
        Please check the following items
    <ul>
        <li>
            photoUrl: ${photoUrl}
        </li>
        <li>
            name: ${name}
        </li>
        <li>
            note: ${note}
        </li>
        <li>
            decoStepsComments: ${decoStepsComments}
        </li>
        <li>
            cnsToxicity: ${cnsToxicity}
        </li>
    </ul>
    <br/>
    Sincerely yours, <br/>
    AquaLink <br/>
    </p>
</@mailer.mail>

