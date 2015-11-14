package org.cmas.presentation.dao.billing;

import org.cmas.presentation.entities.billing.FinSettings;
import org.cmas.util.dao.HibernateDaoImpl;

public class FinSettingsDaoImpl extends HibernateDaoImpl<FinSettings> implements FinSettingsDao{

    @Override
    public FinSettings getFinSettings() {
        return (FinSettings)createCriteria().list().get(0);
    }
}
