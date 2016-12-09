package org.cmas.presentation.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface CaptchaService {

	boolean validateCaptcha(HttpServletRequest servletRequest, HttpServletResponse servletResponse);

    String getReCaptchaPublicKey();
}
