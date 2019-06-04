package org.cmas.presentation.dao.billing;

import org.cmas.entities.loyalty.PaidFeature;
import org.cmas.presentation.dao.DictionaryDataDao;

import java.util.List;

/**
 * Created on Nov 22, 2015
 *
 * @author Alexander Petukhov
 */
public interface PaidFeatureDao extends DictionaryDataDao<PaidFeature> {

    List<PaidFeature> getAll();

    List<PaidFeature> getByIds(List<Long> featureIds);
}
