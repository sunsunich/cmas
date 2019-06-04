package org.cmas.presentation.dao;

import org.cmas.entities.Address;
import org.cmas.util.StringUtil;
import org.hibernate.criterion.Restrictions;

/**
 * Created on May 31, 2019
 *
 * @author Alexander Petukhov
 */
public class AddressDaoImpl extends DictionaryDataDaoImpl<Address> implements AddressDao {

    /*
    private Country country;
    private String zipCode;
    private String city;
    private String street;
    private String house;
     */
    @Override
    public Address getByRawAddress(Address rawAddress) {
        return (Address) createCriteria()
                .createAlias("country", "country")
                .add(Restrictions.eq("city", StringUtil.correctSpaceCharAndTrim(rawAddress.getCity())))
                .add(Restrictions.eq("zipCode", StringUtil.correctSpaceCharAndTrim(rawAddress.getZipCode())))
                .add(Restrictions.eq("street", StringUtil.correctSpaceCharAndTrim(rawAddress.getStreet())))
                .add(Restrictions.eq("house", StringUtil.correctSpaceCharAndTrim(rawAddress.getHouse())))
                .add(Restrictions.eq("country.code",
                                     StringUtil.correctSpaceCharAndTrim(rawAddress.getCountry().getCode())))
                .uniqueResult();
    }
}
