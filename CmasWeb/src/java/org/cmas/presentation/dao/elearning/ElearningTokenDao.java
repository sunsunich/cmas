package org.cmas.presentation.dao.elearning;

import org.cmas.entities.diver.Diver;
import org.cmas.entities.elearning.ElearningToken;
import org.cmas.util.dao.HibernateDao;

import javax.annotation.Nullable;

/**
 * Created on Feb 13, 2019
 *
 * @author Alexander Petukhov
 */
public interface ElearningTokenDao extends HibernateDao<ElearningToken>{

    @Nullable
    ElearningToken getByDiver(Diver diver);

    @Nullable
    ElearningToken getNextNewToken();

    int countAvailableTokens();
}
