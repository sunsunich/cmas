<#import "macro.ftl" as mailer/>
<@mailer.mail title="No InsuranceRequest for Invoice">
<p>Dear AquaLink support,</p>
<p>
    User ${invoice.diver.firstName} ${invoice.diver.lastName}  <br />
    email: ${invoice.diver.email} <br />
    has payed for InsuranceRequest, but there is no InsuranceRequest for this user to match this Invoice! <br />
    Invoice number: ${invoice.externalInvoiceNumber} <br />
    <br />
    Sincerely yours, <br />
    AquaLink <br />
</p>
</@mailer.mail>

