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

    @SuppressWarnings({"HardcodedFileSeparator", "StringConcatenation"})
    @Override
    public String determineTargetUrl(
            SavedRequest savedRequest, HttpServletRequest httpServletRequest, Authentication authentication
    ) {
        for (SpringRole springRole : SpringRole.values()) {
            if (authenticationService.isGranted(authentication, springRole)) {
                return "/login.html?redirectUrl=" + springRole.getIndexUrl();
            }
        }
        return "/index.html";
    }

    @Required
    public void setAuthenticationService(CommonAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
}
