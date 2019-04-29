package org.cmas.presentation.dao.billing;

import org.cmas.entities.fin.LoyaltyProgramItem;
import org.cmas.presentation.dao.DictionaryDataDaoImpl;
import org.hibernate.criterion.Order;

import java.util.List;

/**
 * Created on Apr 28, 2019
 *
 * @author Alexander Petukhov
 */
public class LoyaltyProgramItemDaoImpl extends DictionaryDataDaoImpl<LoyaltyProgramItem> implements LoyaltyProgramItemDao {

    @Override
    public List<LoyaltyProgramItem> getAll() {
        //noinspection unchecked
        return createNotDeletedCriteria().addOrder(Order.asc("id")).list();
    }
}