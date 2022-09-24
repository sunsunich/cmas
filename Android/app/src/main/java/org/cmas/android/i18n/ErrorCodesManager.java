package org.cmas.android.i18n;

import org.cmas.android.storage.MobileDatabase;
import org.cmas.android.storage.dao.ErrorCodeDao;
import org.cmas.android.storage.entities.i18n.ErrorCode;
import org.cmas.service.EntityPersister;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ErrorCodesManager implements EntityPersister<ErrorCode> {

    private final Map<String, String> codesMapping = new HashMap<>();

    public void loadErrorCodesToMemory() {
        ErrorCodeDao errorCodeDao = MobileDatabase.getInstance().getErrorCodeDao();
        for (ErrorCode errorCode : errorCodeDao.getAll()) {
            codesMapping.put(errorCode.code, errorCode.value);
        }
    }

    @Override
    public void persist(@Nullable ErrorCode container) {
        if (container == null) {
            return;
        }
        MobileDatabase.getInstance().getErrorCodeDao().saveOrUpdate(container);
    }

    @Override
    public void persist(@Nullable Collection<ErrorCode> containerCollection) {
        if (containerCollection == null) {
            return;
        }
        for (ErrorCode container : containerCollection) {
            persist(container);
        }
    }

    public boolean hasData(){
        return !codesMapping.isEmpty();
    }

    public String getByCode(String code) {
        return codesMapping.get(code);
    }
}
