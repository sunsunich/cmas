<#import "macro.ftl" as mailer/>
<@mailer.mail title="CMAS AQUALINK: Card approval request submitted">
    <p>Dear ${federation.name},</p>
    <p>
        Diver ${diver.firstName} ${diver.lastName} requests your approval of their CMAS certificate.
    </p>
    <p>
        To review and approve or decline the diver's request please do the following:
    <ol>
        <li>
            login to your account at CMAS AquaLink Federation admin portal<br/>
            <@mailer.href url="${context_path}/login-form.html"/><br/>
        </li>
        <li>
            after successful login open the link below<br/>
            <@mailer.href url="${context_path}/fed/viewCardApprovalRequest.html?requestId=${id}"/>
        </li>
        <li>
            review certificate approval request and fill in the missing fields in the form
        </li>
        <li>
            to approve the request press the "Approve request" button at the bottom of the page
        </li>
        <li>
            to decline the request press the "Decline request" button at the bottom of the page
        </li>
    </ol>
    <br/>
    You can review all the other certificate approval requests at your account at CMAS AquaLink Federation admin portal
    <br/>
    <@mailer.href url="${context_path}/fed/cardApprovalRequests.html"/><br/>
    <br/>
    Sincerely yours, <br/>
    AquaLink <br/>
    </p>
</@mailer.mail>

