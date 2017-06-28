<#import "macro.ftl" as mailer/>
<@mailer.mail title="Instructor has verified your dive">
<p>Dear ${request.from.firstName} ${request.from.lastName},</p>
<p>Instructor ${request.to.firstName} ${request.to.lastName} has verified your dive made on ${diveDate}</p>
<p>
    To view this dive log in to CMASDATA account and open your Logbook:<br />
    <br />
    <@mailer.href url="${context_path}/secure/showLogbook.html"/><br />
    <br />
    If the link does not open, copy it to the browser command line.<br />
    <br />
    Sincerely yours, <br />
    CMASDATA <br />
</p>
</@mailer.mail>
