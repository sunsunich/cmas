package org.cmas.util.presentation.spring;

import org.springframework.security.Authentication;
import org.springframework.security.event.authorization.AbstractAuthorizationEvent;

public class LogoutEvent extends AbstractAuthorizationEvent {    

    public LogoutEvent(Authentication authentication) {
        super(authentication);
    }
}
