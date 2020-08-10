<#import "macro.ftl" as mailer/>
<@mailer.mail title="Feedback submitted">
    <p>Dear ${diver.firstName} ${diver.lastName},</p>
    <p>
        Your feedback item with id = ${id} has been submitted<br/>
        Please find below the details of the item:
    <ul>
        <li>
            text: ${text}
        </li>
        <#if imageUrl1??>
            <li>
                image1 url: ${imageUrl1}
            </li>
        </#if>
        <#if imageUrl2??>
            <li>
                image2 url: ${imageUrl2}
            </li>
        </#if>
        <#if spotName??>
            <li>
                spot Name: ${spotName}
            </li>
        </#if>
    </ul>
    <br/>
    Sincerely yours, <br/>
    AquaLink <br/>
    </p>
</@mailer.mail>

