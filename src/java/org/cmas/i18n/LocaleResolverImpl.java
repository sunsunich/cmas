package org.cmas.i18n;

import org.cmas.presentation.entities.user.BackendUser;
import org.cmas.util.text.StringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 *
 */
public class LocaleResolverImpl implements CustomLocaleResolver {

	static final Locale ca = new Locale("ru");


    @Override
	public Locale resolveLocale(HttpServletRequest httpServletRequest) {
		return ca;
	}

	@Override
	public void setLocale(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Locale locale) {
		// no operation
	}

	@Override
    public Locale getDefaultLocale() {
		return ca;
	}

	@Override
    public Locale getCurrentLocale() {
		return ca;
	}

    @Override
    public Locale getLocale(BackendUser user) {
//        Locale result;
//        if (user != null && user.get() != null) {
//            result = user.getLocale();
//        } else {
//            result = getDefaultLocale();
//        }
 //       log.debug("locale for user " + user + ": " + result);
        //noinspection CallToSimpleGetterFromWithinClass
        return getDefaultLocale();
    }

	@Override
    public List<String> buildCandidateNames(String resourceName, Locale current) {
		List<String> candList = buildCandidateLocales(current);
		List<String> result = new ArrayList<String>();
		for (String cand : candList) {
			String path = cand + (cand.length() == 0 ? "" : "/") + resourceName;
			result.add(path);
		}
		return result;
	}

	@Override
    @SuppressWarnings({"MethodWithMoreThanThreeNegations"})
	public List<String> buildCandidateLocales(Locale current) {
		List<String> result = new ArrayList<String>();
		//current locale
		if (!StringUtil.isEmpty(current.getCountry())) {
			result.add(current.getLanguage() + "_" + current.getCountry());
		}
		result.add(current.getLanguage());
		//default locale
		Locale defaultLocale = ca;
		if (!StringUtil.isEmpty(defaultLocale.getCountry())) {
			String name = defaultLocale.getLanguage() + "_" + defaultLocale.getCountry();
			if (!result.contains(name)) {
				result.add(name);
			}
		}
		String name = defaultLocale.getLanguage();
		if (!result.contains(name)) {
			result.add(name);
		}
		// simple default
		result.add("");
		return result;
	}
}