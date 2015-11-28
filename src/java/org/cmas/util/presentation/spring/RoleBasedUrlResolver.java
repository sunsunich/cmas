package org.cmas.util.presentation.spring;

import org.cmas.util.presentation.CommonAuthentificationService;
import org.cmas.util.presentation.SpringRole;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.Authentication;
import org.springframework.security.ui.TargetUrlResolver;
import org.springframework.security.ui.savedrequest.SavedRequest;

import javax.servlet.http.HttpServletRequest;

public class RoleBasedUrlResolver implements TargetUrlResolver {

    private CommonAuthentificationService authenticationService;

    @Override
    public String determineTargetUrl(
            SavedRequest savedRequest, HttpServletRequest currentRequest, Authentication auth
    ) {
        if (authenticationService.isGranted(auth, SpringRole.ROLE_AMATEUR)) {
            return "/login.html?redirectUrl=/secure/index.html";
        }
        if (authenticationService.isGranted(auth, SpringRole.ROLE_SPORTSMAN)) {
            return "/login.html?redirectUrl=/secure/index.html";
        }
        if (authenticationService.isGranted(auth, SpringRole.ROLE_ADMIN)) {
            return "/login.html?redirectUrl=/admin/index.html";
        }
        return "/index.html";
    }


    @Required
    public void setAuthenticationService(CommonAuthentificationService authenticationService) {
        this.authenticationService = authenticationService;
    }
}
