package org.cmas.util.presentation.spring;

import org.cmas.presentation.entities.user.UserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.Authentication;
import org.springframework.security.event.authentication.AuthenticationSuccessEvent;
import org.springframework.security.ui.WebAuthenticationDetails;
import org.cmas.presentation.dao.user.UserEventDao;
import org.cmas.presentation.entities.user.UserEvent;
import org.cmas.presentation.entities.user.UserEventType;

public class ApplicationListenerImpl implements ApplicationListener{

    @Autowired
    private UserEventDao userEventDao;

    @SuppressWarnings({"ChainOfInstanceofChecks"})
    @Override
    public void onApplicationEvent(ApplicationEvent appEvent) {        
        if (appEvent instanceof AuthenticationSuccessEvent) {
            Authentication authentication = (Authentication) appEvent.getSource();
            String ip = ((WebAuthenticationDetails) authentication.getDetails()).getRemoteAddress();
            UserClient user = (UserClient) authentication.getPrincipal();
            userEventDao.save(new UserEvent(UserEventType.LOGIN, user, ip, ""));
        }
        else if(appEvent instanceof LogoutEvent){
            Authentication authentication = (Authentication) appEvent.getSource();
            WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
            String ip = details == null ? "" : details.getRemoteAddress();
            UserClient user = (UserClient) authentication.getPrincipal();
            userEventDao.save(new UserEvent(UserEventType.LOGOUT, user, ip, ""));
        }

    }
}
