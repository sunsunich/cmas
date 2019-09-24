<#import "macro.ftl" as mailer/>
<@mailer.mail title="Payment to CMAS is successful">
<p>Dear ${invoice.diver.firstName} ${invoice.diver.lastName},</p>
<p>
    your payment for Aqualink <#if hasGoldFeature>GOLD </#if>membership fee was successful! <br />
    <br />
    Sincerely yours, <br />
    AquaLink <br />
</p>
</@mailer.mail>


