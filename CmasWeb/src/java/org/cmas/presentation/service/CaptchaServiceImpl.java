package org.cmas.presentation.service;

import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;
import org.cmas.util.http.Cookies;
import org.springframework.beans.factory.annotation.Required;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CaptchaServiceImpl implements CaptchaService{

    //30 mins
    private static final int CAPTCHA_COOKIE_AGE = 60*2;
    private static final String CAPTCHA_COOKIE_NAME = "CAPTCHA_COOKIE";

    private String reCaptchaPrivateKey;

    private String reCaptchaPublicKey;

	@Override
    public boolean validateCaptcha(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {

        String cookieValue = Cookies.getCookieValueByName(servletRequest.getCookies(), CAPTCHA_COOKIE_NAME);
        if(cookieValue != null){
            return true;
        }

        String challenge = servletRequest.getParameter("recaptcha_challenge_field");
        //retrieve the response
        String response = servletRequest.getParameter("recaptcha_response_field");

         if(challenge == null || response == null){
            return false;
        }

        // Validate the reCAPTCHA
        String remoteAddr = servletRequest.getRemoteAddr();
        ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
        reCaptcha.setPrivateKey(reCaptchaPrivateKey);

        ReCaptchaResponse reCaptchaResponse =
                reCaptcha.checkAnswer(remoteAddr, challenge, response);

        if (reCaptchaResponse.isValid()) {
            Cookie cookie
                    = Cookies.createCookie(CAPTCHA_COOKIE_NAME, String.valueOf(Math.random()), CAPTCHA_COOKIE_AGE);
            servletResponse.addCookie(cookie);
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
