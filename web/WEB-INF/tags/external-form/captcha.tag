<%@ tag body-content="empty" pageEncoding="UTF-8" %>
<%@ attribute name="reCaptchaPublicKey" required="true" %>


<script type="text/javascript">
        var RecaptchaOptions = {
                
                lang : 'ru', // Unavailable while writing this code (just for audio challenge)
                theme : 'clean' // Make sure there is no trailing ',' at the end of the RecaptchaOptions dictionary
        };
</script>
<script type="text/javascript"
   src="http://api.recaptcha.net/challenge?k=${reCaptchaPublicKey}">
</script>
<noscript>


   <iframe src="http://api.recaptcha.net/noscript?k=${reCaptchaPublicKey}"
       height="300" width="500" frameborder="1"></iframe><br>
   <textarea name="recaptcha_challenge_field" rows="3" cols="40">
   </textarea>
   <input type="hidden" name="recaptcha_response_field"
       value="manual_challenge">
</noscript>

