package org.cmas.presentation.service.user;

import org.cmas.entities.diver.Diver;
import org.cmas.entities.sport.NationalFederation;

import java.io.InputStream;

/**
 * Created on Apr 29, 2016
 *
 * @author Alexander Petukhov
 */
public interface DiverService extends UserService<Diver> {

    void uploadDivers(NationalFederation federation, InputStream file) throws Exception;
}
