package org.cmas.presentation.service.loyalty.bf;

import org.cmas.entities.Address;

/**
 * Created on May 31, 2019
 *
 * @author Alexander Petukhov
 */
public class BalticFinanceAddress {

    public String country;
    public String region;
    public String zip;
    public String city;
    public String street;
    public String number;

    /*
      "country" : "DE",
          "region" : "SH",
          "zip" : "24955",
          "city" : "Harrislee",
          "street" : "Werkstraße",
          "number" : "12"
     */

    public BalticFinanceAddress(Address address) {
        country = address.getCountry().getIso3166_1_alpha_2_code();
        region = address.getRegion();
        zip = address.getZipCode();
        city = address.getCity();
        street = address.getStreet();
        number = address.getHouse();
    }
}
