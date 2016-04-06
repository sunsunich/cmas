package org.cmas.service.social;

import android.content.Context;
import org.cmas.MockUtil;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.diver.DiverType;

import java.util.List;

/**
 * Created on Jan 11, 2016
 *
 * @author Alexander Petukhov
 */
public class DiverSearchServiceImpl implements DiverSearchService {
    @Override
    public List<Diver> searchDivers(Context context, String name, DiverType diverType) {
        return MockUtil.searchUsers(name, diverType);
    }
}
