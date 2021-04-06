package org.cmas.android.ui.signin;

import org.cmas.android.storage.entities.Country;
import org.cmas.android.storage.entities.sport.NationalFederation;

import java.util.List;

public class RegistrationInitData {

    public final List<Country> countries;

    public final List<NationalFederation> nationalFederations;

    public RegistrationInitData(List<Country> countries, List<NationalFederation> nationalFederations) {
        this.countries = countries;
        this.nationalFederations = nationalFederations;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public List<NationalFederation> getNationalFederations() {
        return nationalFederations;
    }
}
