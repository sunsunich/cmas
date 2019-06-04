package org.cmas.presentation.dao;

import org.cmas.entities.Address;

/**
 * Created on Nov 22, 2015
 *
 * @author Alexander Petukhov
 */
public interface AddressDao extends DictionaryDataDao<Address> {

    Address getByRawAddress(Address rawAddress);
}
