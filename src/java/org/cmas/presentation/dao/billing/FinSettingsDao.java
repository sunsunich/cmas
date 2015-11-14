package org.cmas.presentation.dao.billing;

import org.cmas.presentation.entities.billing.FinSettings;
import org.cmas.util.dao.HibernateDao;

public interface FinSettingsDao extends HibernateDao<FinSettings>{

    FinSettings getFinSettings();
}
