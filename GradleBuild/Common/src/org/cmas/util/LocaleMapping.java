package org.cmas.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created on Dec 06, 2016
 *
 * @author Alexander Petukhov
 */
public final class LocaleMapping {

    private static final Map<String, String> ISO3_COUNTRY_CODE_TO_ISO2_COUNTRY_CODE_MAP;

    static {
        String[] countries = Locale.getISOCountries();
        ISO3_COUNTRY_CODE_TO_ISO2_COUNTRY_CODE_MAP = new HashMap<>(countries.length);
        for (String iso2Country : countries) {
            String iso3Country = new Locale("", iso2Country).getISO3Country();
            ISO3_COUNTRY_CODE_TO_ISO2_COUNTRY_CODE_MAP.put(iso3Country, iso2Country);
        }
    }

    private LocaleMapping() {
    }

    public static String getIso2CountryCode(String iso3CointryCode) {
        return ISO3_COUNTRY_CODE_TO_ISO2_COUNTRY_CODE_MAP.get(iso3CointryCode);
    }
}
