package org.cmas.entities;

public enum Role {
    ROLE_AMATEUR("ROLE_AMATEUR", "amateur"),
    ROLE_ATHLETE("ROLE_ATHLETE", "professional athlete"),
    ROLE_DIVER("ROLE_DIVER", "diver"),
    ROLE_FEDERATION_ADMIN("ROLE_FEDERATION_ADMIN", "federation admin"),
    ROLE_ADMIN("ROLE_ADMIN", "admin");


    private final String name;
    private final String label;

    Role(String name, String label) {
        this.name = name;
        this.label = label;

    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }
}