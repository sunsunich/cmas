package org.cmas.backend.xls;

import org.cmas.entities.diver.Diver;

import java.io.File;
import java.util.Collection;

/**
 * Created on Apr 26, 2016
 *
 * @author Alexander Petukhov
 */
public interface DiverXlsParser {

    Collection<Diver> getDivers(File file) throws Exception;
}
