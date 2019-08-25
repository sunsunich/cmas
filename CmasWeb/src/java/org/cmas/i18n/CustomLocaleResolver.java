package org.cmas.i18n;

import org.cmas.presentation.entities.user.BackendUser;
import org.springframework.web.servlet.LocaleResolver;

import java.util.List;
import java.util.Locale;

public interface CustomLocaleResolver extends LocaleResolver {

     // must always work
    Locale getLocale(BackendUser user);
    // must always work in context of http-request
    Locale getCurrentLocale();

    Locale getDefaultLocale(); // default locale in configuration

    List<String> buildCandidateNames(String resourceName, Locale current);

    List<String> buildCandidateLocales(Locale current);

}
