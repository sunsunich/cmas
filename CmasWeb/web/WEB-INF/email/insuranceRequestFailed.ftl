<#import "macro.ftl" as mailer/>
<@mailer.mail title="Failed to deliver InsuranceRequest">
    <p>Dear AquaLink support,</p>
    <p>
        User ${insuranceRequest.diver.firstName} ${insuranceRequest.diver.lastName} <br/>
        email: ${insuranceRequest.diver.email} <br/>
        has payed for InsuranceRequest, but we failed to deliver it to Baltic Finance! <br/>
        <br/>
        Reason: ${reason}<br/>
        <br/>
        Please contact Baltic Finance asap:
    <ul>
        <li>
            Stefan Erichsen: stefan@balticfinance.com
        </li>
        <li>
            Christian Krützfeldt: christian@balticfinance.com
        </li>
    </ul>
    <br/>
    InsuranceRequest id: ${insuranceRequest.id} <br/>
    <br/>
    Sincerely yours, <br/>
    AquaLink <br/>
    </p>
</@mailer.mail>

