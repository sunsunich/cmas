package org.cmas.util.mail;

import java.util.Locale;

/**
 */
public interface TemplateRenderer<T> {

    String renderText(String templateName, Locale locale, T... modelMembers);

}
