package org.cmas.android.storage.convert;

import org.cmas.android.storage.MobileDatabase;
import org.cmas.android.storage.dao.NationalFederationDao;
import org.cmas.android.storage.entities.sport.NationalFederation;

public class NationalFederationPersisterImpl extends DictionaryEntityPersisterImpl<NationalFederationDao, org.cmas.entities.sport.NationalFederation, NationalFederation> {

    @Override
    protected NationalFederationDao getDao() {
        return MobileDatabase.getInstance().getNationalFederationDao();
    }

    @Override
    protected Class<NationalFederation> getEntityClass() {
        return NationalFederation.class;
    }
}
