package org.cmas.presentation.service;

import com.google.myjson.Gson;
import org.apache.commons.httpclient.NameValuePair;
import org.cmas.json.SimpleGsonResponse;
import org.cmas.util.StringUtil;
import org.cmas.util.http.Cookies;
import org.cmas.util.http.HttpUtil;
import org.cmas.util.http.SimpleHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CaptchaServiceImpl implements CaptchaService {

    private static final Logger LOG = LoggerFactory.getLogger(CaptchaServiceImpl.class);

    //30 mins
    private static final int CAPTCHA_COOKIE_AGE = 60 * 2;
    private static final String CAPTCHA_COOKIE_NAME = "CAPTCHA_COOKIE";
    private static final String RECAPTCHA_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    private String reCaptchaPrivateKey;

    private String reCaptchaPublicKey;

    @Override
    public boolean validateCaptcha(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        String cookieValue = Cookies.getCookieValueByName(servletRequest.getCookies(), CAPTCHA_COOKIE_NAME);
        if (cookieValue != null) {
            return true;
        }
        String response = servletRequest.getParameter("g-recaptcha-response");
        if (StringUtil.isTrimmedEmpty(response)) {
            return false;
        }
        /*
        http://stackoverflow.com/questions/17559671/post-requests-from-a-servlet
         */
        SimpleGsonResponse validationResponse;
        try {
            // Validate the reCAPTCHA
        /*
        secret	Required. The shared key between your site and reCAPTCHA.
        response	Required. The user response token provided by reCAPTCHA, verifying the user on your site.
        remoteip	Optional. The user's IP address.
         */
            validationResponse = new Gson().fromJson(SimpleHttpClient.sendHTTPPostRequest(
                    RECAPTCHA_VERIFY_URL,
                    new NameValuePair[]{
                            new NameValuePair("secret", reCaptchaPrivateKey),
                            new NameValuePair("response", response),
                            new NameValuePair("remoteip", HttpUtil.getIP(servletRequest))
                    }
            ), SimpleGsonResponse.class);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return false;
        }

        if (validationResponse.isSuccess()) {
            servletResponse.addCookie(Cookies.createCookie(
                    CAPTCHA_COOKIE_NAME, String.valueOf(StrictMath.random()), CAPTCHA_COOKIE_AGE
            ));
            return true;
        } else {
            return false;
        }

    }

    @Required
    public void setReCaptchaPrivateKey(String reCaptchaPrivateKey) {
        this.reCaptchaPrivateKey = reCaptchaPrivateKey;
    }

    @Required
    public void setReCaptchaPublicKey(String reCaptchaPublicKey) {
        this.reCaptchaPublicKey = reCaptchaPublicKey;
    }

    @Override
    public String getReCaptchaPublicKey() {
        return reCaptchaPublicKey;
    }
}
