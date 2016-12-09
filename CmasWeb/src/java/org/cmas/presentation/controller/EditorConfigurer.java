package org.cmas.presentation.controller;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.mail.javamail.InternetAddressEditor;

import javax.mail.internet.InternetAddress;


public class EditorConfigurer implements PropertyEditorRegistrar {
    @Override
    public void registerCustomEditors(PropertyEditorRegistry registry) {
        registry.registerCustomEditor(InternetAddress.class, new InternetAddressEditor());
    }
}
