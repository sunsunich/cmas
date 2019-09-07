<#import "macro.ftl" as mailer/>
<@mailer.mail title="Your order is placed">
<p>Dear ${diver.firstName} ${diver.lastName},</p>
<p>
    your order for ${cameraName} has been placed.
</p>
<p>
    Please wait for SUBAL representatives to contact you. <br />
    <br />
    Please address all your enquiries regarding this order to SUBAL representatives using this email: ${sendToEmail}<br />
    <br />
    Your reference number for this order is: ${referenceNumber}<br />
    <br />
    Sincerely yours, <br />
    AquaLink <br />
</p>
</@mailer.mail>        

