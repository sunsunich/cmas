package org.cmas.presentation.model.admin;

@SuppressWarnings({"EnumeratedConstantNamingConvention"})
public enum YesNoEmpty {
    YES("Yes"), NO("No"), EMPTY("");

    private String name;

    YesNoEmpty(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

