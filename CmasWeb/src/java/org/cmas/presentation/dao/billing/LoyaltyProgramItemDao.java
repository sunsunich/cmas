package org.cmas.presentation.dao.billing;

import org.cmas.entities.loyalty.LoyaltyProgramItem;
import org.cmas.presentation.dao.DictionaryDataDao;

import java.util.List;

/**
 * Created on Nov 22, 2015
 *
 * @author Alexander Petukhov
 */
public interface LoyaltyProgramItemDao extends DictionaryDataDao<LoyaltyProgramItem> {

    List<LoyaltyProgramItem> getAll();
}
