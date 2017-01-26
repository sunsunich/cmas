package org.cmas.presentation.dao.logbook;

import org.cmas.entities.logbook.ScubaTank;
import org.cmas.util.dao.HibernateDaoImpl;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created on Jan 10, 2017
 *
 * @author Alexander Petukhov
 */
@SuppressWarnings("unchecked")
public class ScubaTankDaoImpl extends HibernateDaoImpl<ScubaTank> implements ScubaTankDao {
    @Override
    public List<ScubaTank> getScubaTanksByIds(List<Long> scubaTankIds) {
        return createCriteria().add(Restrictions.in("id", scubaTankIds)).list();
    }
}
