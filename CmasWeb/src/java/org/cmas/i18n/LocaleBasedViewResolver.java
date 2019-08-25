package org.cmas.i18n;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.io.File;
import java.util.List;
import java.util.Locale;


public class LocaleBasedViewResolver extends InternalResourceViewResolver {

    // Logger instance for class
    private static final Logger log = LoggerFactory.getLogger(LocaleBasedViewResolver.class);

    private CustomLocaleResolver localeResolver;

    public void setLocaleResolver(CustomLocaleResolver localeResolver) {
        this.localeResolver = localeResolver;
    }

    @Override
    protected View createView(String viewName, Locale locale) throws Exception {
        return super.createView(viewName, locale);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected AbstractUrlBasedView buildView(String viewName) throws Exception {
        String localeViewName = buildLocalizedName(viewName);
        AbstractUrlBasedView result = super.buildView(localeViewName);
        log.debug("view name=" + localeViewName);
        return result;
    }

    public String buildLocalizedName(String viewName) {
        return findFirstExisting(localeResolver.buildCandidateNames(viewName, localeResolver.getCurrentLocale()));
    }

    public String buildLocalizedPath(String viewName) {
        String s = findFirstExisting(localeResolver.buildCandidateNames(viewName, localeResolver.getCurrentLocale()));
        return getPrefix() + s + getSuffix();
    }

    private String findFirstExisting(List<String> strings) {// not empty
        File docBase = new File(getServletContext().getRealPath(""));
        for (String resourcePath : strings) {
            File resourceFile = new File(docBase, getPrefix() + resourcePath + getSuffix());
            if (resourceFile.exists()) {
                return resourcePath;
            }
        }
        return strings.get(strings.size()-1); // last
    }

}
