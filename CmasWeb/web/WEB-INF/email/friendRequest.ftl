<#import "macro.ftl" as mailer/>
<@mailer.mail title="Friend request">
<p>Dear ${request.to.firstName} ${request.to.lastName},</p>
<p>${request.from.firstName} ${request.from.lastName} has sent you a friend request.</p>
<p>
    To accept or reject this request log in to your CMAS Aqua link account, go to "FRIENDS" section and open "Friend requests" tab:<br />
    <br />
    <@mailer.href url="${context_path}/secure/social.html"/><br />
    <br />
    If the link does not open, copy it to the browser command line.<br />
    <br />
    Sincerely yours, <br />
    CMAS Aqua link <br />
</p>
</@mailer.mail>        

