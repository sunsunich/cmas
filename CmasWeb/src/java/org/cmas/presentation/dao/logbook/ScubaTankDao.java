package org.cmas.presentation.dao.logbook;

import org.cmas.entities.logbook.ScubaTank;
import org.cmas.util.dao.HibernateDao;

import java.util.List;

/**
 * Created on Jan 10, 2017
 *
 * @author Alexander Petukhov
 */
public interface ScubaTankDao extends HibernateDao<ScubaTank> {

    List<ScubaTank> getScubaTanksByIds(List<Long> scubaTankIds);
}
