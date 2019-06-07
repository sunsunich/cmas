package org.cmas.presentation.service.loyalty.bf;

import org.cmas.entities.diver.Diver;
import org.cmas.entities.loyalty.InsuranceRequest;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created on May 31, 2019
 *
 * @author Alexander Petukhov
 */
public class BalticFinanceInsuranceRequest {

    private static final TimeZone UTC = TimeZone.getTimeZone("UTC");
    private static final String BF_DATE_FORMAT = "YYYY-MM-DD'T'HH:mm:ssX";
    public long id;
    public String firstname;
    public String lastname;
    public String gender;
    // "YYYY-MM-DD"
    public String dateOfBirth;
    public String signInDate;

    public String email;
    /*
    language for the policy of the customer, can be EN, ES or FR
     */
    public String language;

    public BalticFinanceAddress address;

    BalticFinanceInsuranceRequest(InsuranceRequest insuranceRequest) {
        id = insuranceRequest.getId();
        SimpleDateFormat format = new SimpleDateFormat(BF_DATE_FORMAT, Locale.ENGLISH);
        format.setTimeZone(UTC);
        signInDate = format.format(insuranceRequest.getCreateDate());

        Diver diver = insuranceRequest.getDiver();
        firstname = diver.getFirstName();
        lastname = diver.getLastName();
        dateOfBirth = new SimpleDateFormat("YYYY-MM-DD").format(diver.getDob());
        email = diver.getEmail();
        if ("fr".equalsIgnoreCase(diver.getLocale().getLanguage())) {
            language = "FR";
        } else if ("es".equalsIgnoreCase(diver.getLocale().getLanguage())) {
            language = "ES";
        } else {
            language = "EN";
        }

        gender = insuranceRequest.getGender().name();
        address = new BalticFinanceAddress(insuranceRequest.getAddress());
    }
    /*
    "id" : 42,
      "firstname": "Testibert",
      "lastname" : "Testerson",
      "gender" : "FEMALE",
      "dateOfBirth" : "1988-02-25",
      "address": {
    ...
      },
      "signInDate" : "2019-05-13",
      "email" : "test@balticfinance.com",
      "language" : "EN"
     */
}
