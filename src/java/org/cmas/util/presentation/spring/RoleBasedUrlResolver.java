package org.cmas.util.presentation.spring;

import org.cmas.util.presentation.CommonAuthenticationService;
import org.cmas.util.presentation.SpringRole;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.Authentication;
import org.springframework.security.ui.TargetUrlResolver;
import org.springframework.security.ui.savedrequest.SavedRequest;

import javax.servlet.http.HttpServletRequest;

public class RoleBasedUrlResolver implements TargetUrlResolver {

    private CommonAuthenticationService authenticationService;

    @SuppressWarnings("HardcodedFileSeparator")
    @Override
    public String determineTargetUrl(
            SavedRequest savedRequest, HttpServletRequest httpServletRequest, Authentication authentication
    ) {
        if (authenticationService.isGranted(authentication, SpringRole.ROLE_DIVER)
            || authenticationService.isGranted(authentication, SpringRole.ROLE_DIVER_INSTRUCTOR)
                ) {
            return "/login.html?redirectUrl=/secure/index.html";
        }
        if (authenticationService.isGranted(authentication, SpringRole.ROLE_AMATEUR)) {
            return "/login.html?redirectUrl=/secure/index.html";
        }
        if (authenticationService.isGranted(authentication, SpringRole.ROLE_ATHLETE)) {
            return "/login.html?redirectUrl=/secure/index.html";
        }
        if (authenticationService.isGranted(authentication, SpringRole.ROLE_FEDERATION_ADMIN)) {
            return "/login.html?redirectUrl=/fed/index.html";
        }
        if (authenticationService.isGranted(authentication, SpringRole.ROLE_ADMIN)) {
            return "/login.html?redirectUrl=/admin/index.html";
        }
        return "/index.html";
    }


    @Required
    public void setAuthenticationService(CommonAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
}
