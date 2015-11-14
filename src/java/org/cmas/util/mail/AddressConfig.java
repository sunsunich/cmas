package org.cmas.util.mail;

import java.util.Locale;

/**

 */
public interface AddressConfig {
    String getSiteAddress();

    String getSiteName(Locale locale);

    String getSupportAddr();


    String getQuestionFormAddr();
}
