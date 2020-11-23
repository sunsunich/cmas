<#import "macro.ftl" as mailer/>
<@mailer.mail title="Order from AquaLink member">
<p>Dear Sirs,</p>
<p>
    AquaLink member ${diver.firstName} has placed an order for ${cameraName}. <br/>
    <br/>
    Diver's AquaLink number is ${diver.primaryPersonalCard.printNumber}.<br />
    <br />
    Reference number for this order is: ${referenceNumber}
</p>
<p>
    Please contact ${diver.firstName} using his email: <br/>
    ${diver.email} <br/>
    in order to process the order.
</p>
<p>
    Sincerely yours, <br />
    CMAS<br />
</p>
</@mailer.mail>        

