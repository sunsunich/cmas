<#import "macro.ftl" as mailer/>
<@mailer.mail title="Order from CMAS member">
<p>Dear Sirs,</p>
<p>
    CMAS member ${diver.firstName} from CMAS national federation of ${country.name} has placed an order for ${cameraName}. <br/>
    <br/>
    Diver's CMAS number is ${diver.primaryPersonalCard.printNumber}.
</p>
<p>
    Please contact ${diver.firstName} using his email: <br/>
    ${diver.email} <br/>
    in order to process the order.
</p>
<p>
    Diver can be verified using this online service: <br/>
    <br/>
    <@mailer.href url="${context_path}/diver-verification.html?name=${diver.firstName}&country=${country.code}"/><br />
</p>
<p>
    Sincerely yours, <br />
    CMAS<br />
</p>
</@mailer.mail>        

