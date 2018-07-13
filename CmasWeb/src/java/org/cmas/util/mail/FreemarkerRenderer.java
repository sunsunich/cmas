package org.cmas.util.mail;

import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.cmas.i18n.LocaleResolverImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailPreparationException;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 */
public class FreemarkerRenderer implements TemplateRenderer<ModelAttr> {
    // Logger instance for class
    private static final Logger log = LoggerFactory.getLogger(FreemarkerRenderer.class);
    /**
     * freemarker
     */
    private Configuration configuration;
    private LocaleResolverImpl localeResolver;

    private AddressConfig mailerConfig;

    public void setMailerConfig(AddressConfig mailerConfig) {
        this.mailerConfig = mailerConfig;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public void setLocaleResolver(LocaleResolverImpl localeResolver) {
        this.localeResolver = localeResolver;
    }

    // todo модель не полностью зависит от локали.
    private Map<String, Object> createCommonLocalizedModel(Locale locale) {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("siteAddress", mailerConfig.getSiteAddress());
        model.put("context_path", mailerConfig.getSiteWebAddress(locale));
        model.put("siteName", mailerConfig.getSiteName(locale));
        return model;
    }

    @Override
    public String renderText(String templateName, Locale locale, ModelAttr[] modelMembers) {
        Map<String, Object> model = createCommonLocalizedModel(locale);
        for (ModelAttr modelMember : modelMembers) {
            model.put(modelMember.name, modelMember.data);
        }
        return renderTextInternal(model, templateName, locale);
    }

    private String renderTextInternal(Map<String, Object> model, String template, Locale locale) {
        Locale notNullLocale = locale == null ? localeResolver.getDefaultLocale() : locale;
        String text;
        try {
            Template t = getTemplate(template, notNullLocale);
            text = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
        } catch (Exception e) {
            throw new MailPreparationException(e);
        }
        return text;
    }

    private Template getTemplate(String templateName, Locale notNullLocale) throws IOException {
        List<String> candidates = localeResolver.buildCandidateNames(templateName, notNullLocale);
        TemplateLoader templateLoader = configuration.getTemplateLoader();
        for (String candidate : candidates) {
            Object source = templateLoader.findTemplateSource(candidate);
            if (source != null) {
                return configuration.getTemplate(candidate);
            }
        }
        log.error("Couldn't find template for email template: " + templateName);
        throw new IllegalStateException("Couldn't find template for email");
    }

}
