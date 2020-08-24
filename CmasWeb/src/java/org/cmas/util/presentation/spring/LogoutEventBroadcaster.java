package org.cmas.util.presentation.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.Authentication;
import org.springframework.security.ui.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogoutEventBroadcaster implements LogoutHandler, ApplicationContextAware {

	//private Log log = LogFactory.getLog(LogoutEventBroadcaster.class);

    public static final String AUTH_COOKIE_NAME = "AUTH_COOKIE";
    public static final int AUTH_COOKIE_AGE = -1;

	private ApplicationContext applicationContext;

	/**
	 *
	 */
	public LogoutEventBroadcaster() {
		super();
	}


	@Override
    public void logout(HttpServletRequest arg0, HttpServletResponse arg1, Authentication auth) {
//        Cookie cookie = Cookies.rewriteValueInCookie(arg0.getCookies()
//                , AUTH_COOKIE_NAME
//                , ""
//                , 0);
//        arg1.addCookie(cookie);
        if (auth != null) {
            LogoutEvent event = new LogoutEvent(auth);
            applicationContext.publishEvent(event);
        }
	}


	@Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
